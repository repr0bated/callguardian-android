package com.example.callguardian.screening

import android.content.Context
import com.example.callguardian.ai.HybridScamAnalyzer
import com.example.callguardian.ai.Prompts
import com.example.callguardian.ai.ScamVerdict
import com.example.callguardian.data.db.AiStatus
import com.example.callguardian.data.db.PhoneProfileDao
import com.example.callguardian.data.db.ScreeningEventDao
import com.example.callguardian.data.db.ScreeningEventEntity
import com.example.callguardian.data.settings.LookupPreferences
import com.example.callguardian.firebase.FirebaseBootstrap
import com.example.callguardian.lookup.ReverseLookupManager
import com.example.callguardian.methods.BindingContext
import com.example.callguardian.methods.CapabilityPolicy
import com.example.callguardian.methods.Dispatch
import com.example.callguardian.methods.DispatchResult
import com.example.callguardian.methods.HandlerExtras
import com.example.callguardian.methods.MethodBinder
import com.example.callguardian.methods.MethodDispatcher
import com.example.callguardian.methods.MethodProposal
import com.example.callguardian.methods.PolicyContext
import com.example.callguardian.methods.PolicyDecision
import com.example.callguardian.model.Channel
import com.example.callguardian.model.GuardianMethod
import com.example.callguardian.model.RiskLevel
import com.example.callguardian.model.ScamCategory
import com.example.callguardian.model.VerdictSource
import com.example.callguardian.notifications.AlertNotifier
import com.example.callguardian.policy.SchemaValidator
import com.example.callguardian.service.ContactLookupService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

data class CallDecision(val reject: Boolean, val silence: Boolean, val reason: String)

/**
 * The three-tier pipeline:
 *  1. Rules (instant, offline, background-safe) — always.
 *  2. On-device model via Firebase AI Logic (Gemma 4 / Gemini Nano 4) — private, offline-capable.
 *  3. Cloud Gemini (`guardian.cloud.explain@v1`) — only when the user asks, with redacted input.
 *
 * Every tier ends the same way: it may only *propose* one registry method. The proposal passes
 * JSON Schema validation, the [CapabilityPolicy], and the method's input schema, and then runs
 * through [MethodDispatcher], which writes a receipt.
 */
@Singleton
class ScreeningEngine @Inject constructor(
    @ApplicationContext private val context: Context,
    private val events: ScreeningEventDao,
    private val profiles: PhoneProfileDao,
    private val prefs: LookupPreferences,
    private val contacts: ContactLookupService,
    private val lookup: ReverseLookupManager,
    private val analyzer: HybridScamAnalyzer,
    private val notifier: AlertNotifier,
    private val policy: CapabilityPolicy,
    private val dispatcher: MethodDispatcher,
    private val io: CoroutineDispatcher,
) {
    private val json = Json { ignoreUnknownKeys = false }
    val verdictValidator by lazy { SchemaValidator(asset("schemas/scam_verdict.schema.json"), "scam_verdict.schema.json") }
    private val aiMutex = Mutex() // AICore enforces per-app quotas; serialize requests.

    private fun asset(path: String) = context.assets.open(path).bufferedReader().use { it.readText() }

    fun normalize(number: String?): String? = number?.trim()?.let { n ->
        if (n.contains('@')) n else n.filter { it.isDigit() || it == '+' }.ifBlank { null }
    }

    // ---------------------------------------------------------------- SMS

    suspend fun screenSms(rawNumber: String?, body: String, isTest: Boolean = false, runAi: Boolean = true): Long = withContext(io) {
        val number = normalize(rawNumber)
        val contact = number?.let { runCatching { contacts.lookupContact(it) }.getOrNull() }
        val profile = number?.let { profiles.get(it) }
        val scan = HeuristicScanner.scan(ScanInput(Channel.SMS, number, body, isContact = contact != null))
        val blocked = profile?.shouldBlockMessages() == true
        val wantsAi = runAi && (scan.riskLevel >= RiskLevel.LOW || contact == null)

        val id = events.insert(ScreeningEventEntity(
            channel = Channel.SMS.name, number = number, displayName = contact?.displayName ?: profile?.displayName,
            body = body, isContact = contact != null, isTest = isTest,
            rulesScore = scan.score, rulesRisk = scan.riskLevel.name, rulesFlags = scan.reasons.joinToString("\n"), rulesMicros = scan.elapsedMicros,
            aiStatus = if (wantsAi) AiStatus.PENDING else AiStatus.NOT_RUN,
            risk = (if (blocked) maxOf(scan.riskLevel, RiskLevel.HIGH) else scan.riskLevel).name, category = scan.category.name,
            confidence = scan.score, reasons = scan.reasons.joinToString("\n"),
            userAction = if (blocked) "BLOCKED_BY_LIST" else null, quarantined = blocked,
        ))
        if (!blocked) decideAndDispatch(id, MethodProposal(scan.method, scan.riskLevel, scan.score, scan.reasons, VerdictSource.RULES))
        val saved = events.get(id)!!
        if (!isTest) notifier.notifyEvent(saved, aiPending = wantsAi)
        if (wantsAi) {
            // AICore refuses background inference; if so the event stays PENDING and runs when the
            // app is next opened (or the alert is tapped).
            analyzeOnDevice(id, fromBackground = !AppForeground.isForeground, notify = !isTest)
        }
        id
    }

    // ---------------------------------------------------------------- Calls

    /** Must finish well inside CallScreeningService's 5 s budget. Rules + lookup only. */
    suspend fun screenCall(rawNumber: String?, callerIdHidden: Boolean, startedAt: Long = System.currentTimeMillis()): Pair<CallDecision, Long> = withContext(io) {
        val number = normalize(rawNumber)
        val profile = number?.let { profiles.get(it) }
        val contact = number?.let { runCatching { contacts.lookupContact(it) }.getOrNull() }
        val settings = prefs.current()

        if (profile?.shouldBlockCalls() == true) {
            val id = events.insert(ScreeningEventEntity(
                channel = Channel.CALL.name, number = number, displayName = profile.displayName, risk = RiskLevel.HIGH.name,
                proposedAction = GuardianMethod.REJECT_CALL.alias, finalAction = GuardianMethod.REJECT_CALL.alias, policyDispatch = Dispatch.AUTO.name,
                autoApplied = true, gateNotes = "Sender is on your block list.", userAction = "BLOCKED_BY_LIST",
            ))
            dispatcher.dispatch(GuardianMethod.REJECT_CALL,
                MethodBinder.bind(GuardianMethod.REJECT_CALL, BindingContext(id, Channel.CALL, number, profile.displayName, RiskLevel.CRITICAL, ScamCategory.NONE, listOf("On your block list"))),
                id, VerdictSource.RULES, Dispatch.AUTO.name, listOf("Sender is on your block list."), HandlerExtras(System.currentTimeMillis() - startedAt))
            return@withContext CallDecision(true, false, "On your block list") to id
        }

        val lookupResult = if (contact == null && number != null && settings.anyLookupConfigured) lookup.lookup(number, 1_500) else null
        val recent = number?.let { events.countCallsSince(it, System.currentTimeMillis() - 15 * 60_000) } ?: 0
        val scan = HeuristicScanner.scan(ScanInput(
            channel = Channel.CALL, number = number, isContact = contact != null, callerIdHidden = callerIdHidden,
            lookupSpamScore = lookupResult?.spamScore, lookupLineType = lookupResult?.lineType,
            lookupTags = lookupResult?.tags.orEmpty(), recentCallsFromNumber = recent,
        ))
        val summary = lookupResult?.let { listOfNotNull(it.displayName, it.carrier, it.region, it.lineType, it.spamScore?.let { s -> "spam score $s" }).joinToString(" · ") }
        val wantsAi = contact == null && scan.riskLevel >= RiskLevel.LOW
        val id = events.insert(ScreeningEventEntity(
            channel = Channel.CALL.name, number = number, displayName = contact?.displayName ?: lookupResult?.displayName,
            isContact = contact != null, lookupSummary = summary,
            rulesScore = scan.score, rulesRisk = scan.riskLevel.name, rulesFlags = scan.reasons.joinToString("\n"), rulesMicros = scan.elapsedMicros,
            aiStatus = if (wantsAi) AiStatus.PENDING else AiStatus.NOT_RUN,
            risk = scan.riskLevel.name, category = scan.category.name, confidence = scan.score, reasons = scan.reasons.joinToString("\n"),
        ))
        var proposal = MethodProposal(scan.method, scan.riskLevel, scan.score, scan.reasons, VerdictSource.RULES)
        if (proposal.method == GuardianMethod.REJECT_CALL && !settings.silenceSuspiciousCalls) proposal = proposal.copy(method = GuardianMethod.NONE)
        val results = decideAndDispatch(id, proposal, HandlerExtras(System.currentTimeMillis() - startedAt))
        val e = events.get(id)!!
        val blockedNow = results.any { it.first == GuardianMethod.BLOCK_NUMBER && it.second.ok }
        val rejectOut = results.firstOrNull { it.first == GuardianMethod.REJECT_CALL && it.second.ok }?.second?.output?.toString()
        val reject = blockedNow || rejectOut?.contains("\"REJECT\"") == true
        val silence = !reject && rejectOut?.contains("\"SILENCE\"") == true
        CallDecision(reject, silence, e.gateNotes.lineSequence().firstOrNull().orEmpty()) to id
    }

    suspend fun afterCall(id: Long) {
        val e = events.get(id) ?: return
        if (e.isContact && e.finalAction == GuardianMethod.NONE.alias) return
        if (RiskLevel.valueOf(e.risk) >= RiskLevel.LOW || e.userAction == "BLOCKED_BY_LIST") notifier.notifyEvent(e, aiPending = e.aiStatus == AiStatus.PENDING)
    }

    // ---------------------------------------------------------------- policy + dispatch

    private suspend fun policyContext(e: ScreeningEventEntity): PolicyContext {
        val s = prefs.current()
        val trusted = e.number?.let { profiles.get(it)?.trusted } == true
        return PolicyContext(Channel.valueOf(e.channel), e.isContact, trusted, s.autoBlock, s.autoBlockThreshold, FirebaseBootstrap.cloudConfigured)
    }

    fun binding(e: ScreeningEventEntity) = BindingContext(
        eventId = e.id, channel = Channel.valueOf(e.channel), number = e.number, displayName = e.displayName,
        risk = runCatching { RiskLevel.valueOf(e.risk) }.getOrDefault(RiskLevel.SAFE),
        category = runCatching { ScamCategory.valueOf(e.category) }.getOrDefault(ScamCategory.NONE),
        reasons = e.reasons.lines().filter { it.isNotBlank() },
        redactedText = redactedPreview(e),
        onDeviceOpinion = if (e.aiStatus == AiStatus.DONE) "${e.risk}, ${e.category}, confidence ${e.confidence}" else null,
    )

    /** Policy-check a proposal, persist the decision, auto-run AUTO methods, record DENY receipts. */
    private suspend fun decideAndDispatch(id: Long, p: MethodProposal, extras: HandlerExtras = HandlerExtras()): List<Pair<GuardianMethod, DispatchResult>> {
        val e = events.get(id) ?: return emptyList()
        val d: PolicyDecision = policy.decide(p, policyContext(e))
        events.update(e.copy(
            decidedBy = p.source.name, proposedAction = d.proposed.alias, finalAction = d.method.alias,
            policyDispatch = d.dispatch.name, gateNotes = d.notes.joinToString("\n"), autoApplied = d.dispatch == Dispatch.AUTO,
        ))
        val ctx = binding(events.get(id)!!)
        val results = mutableListOf<Pair<GuardianMethod, DispatchResult>>()
        if (d.proposed != GuardianMethod.NONE && (d.dispatch == Dispatch.DENY || d.substituted)) {
            dispatcher.recordDenied(d.proposed, MethodBinder.bind(d.proposed, ctx), id, p.source, d.notes)
        }
        val canAuto = d.dispatch == Dispatch.AUTO && d.method != GuardianMethod.NONE && (!e.isTest || d.method.isSafeForTests())
        if (canAuto) {
            results += d.method to dispatcher.dispatch(d.method, MethodBinder.bind(d.method, ctx), id, p.source, Dispatch.AUTO.name, d.notes, extras)
        }
        return results
    }

    /** Test-lab events must not block real numbers. */
    private fun GuardianMethod.isSafeForTests() = this == GuardianMethod.QUARANTINE_SMS

    /** A user-confirmed dispatch from the schema-rendered confirmation sheet. */
    suspend fun runUserMethod(eventId: Long?, method: GuardianMethod, input: JsonObject): DispatchResult = withContext(io) {
        val r = dispatcher.dispatch(method, input, eventId, VerdictSource.USER, "USER_CONFIRMED")
        if (r.ok && eventId != null) {
            events.get(eventId)?.let { e ->
                events.update(e.copy(userAction = method.alias))
                if (method != GuardianMethod.EXPLAIN_CLOUD) notifier.cancel(eventId)
            }
        }
        r
    }

    /** Policy view of a proposal without dispatching (used to gate the cloud's suggested method). */
    suspend fun evaluate(e: ScreeningEventEntity, method: GuardianMethod, source: VerdictSource): PolicyDecision =
        policy.decide(MethodProposal(method, runCatching { RiskLevel.valueOf(e.risk) }.getOrDefault(RiskLevel.SAFE), e.confidence,
            e.reasons.lines().filter { it.isNotBlank() }, source), policyContext(e))

    suspend fun runUserMethodForEvent(eventId: Long, method: GuardianMethod): DispatchResult? {
        val e = events.get(eventId) ?: return null
        return runUserMethod(eventId, method, defaultInput(e, method))
    }

    /** List maintenance from Settings (not a model-callable method). */
    suspend fun unlist(number: String) = withContext(io) {
        profiles.get(number)?.let { profiles.upsert(it.copy(blockMode = com.example.callguardian.model.BlockMode.NONE.name, trusted = false, updatedAt = System.currentTimeMillis())) }
    }

    fun defaultInput(e: ScreeningEventEntity, method: GuardianMethod): JsonObject = MethodBinder.bind(method, binding(e))

    // ---------------------------------------------------------------- Tier 2: on-device

    suspend fun analyzeOnDevice(id: Long, fromBackground: Boolean = false, notify: Boolean = true): ScreeningEventEntity? = withContext(io) {
        val e = events.get(id) ?: return@withContext null
        val settings = prefs.current()
        val channel = Channel.valueOf(e.channel)
        val rules = HeuristicScanner.scan(ScanInput(channel, e.number, e.body, e.isContact))
        val sender = e.displayName ?: e.number ?: "hidden number"
        val prompt = Prompts.triage(channel, sender, e.isContact, e.body, e.lookupSummary, rules)

        events.update(e.copy(aiStatus = AiStatus.RUNNING, aiError = null))
        val run = try {
            aiMutex.withLock { withTimeout(25_000) { analyzer.triage(prompt, settings.modelChoice, settings.privateMode) } }
        } catch (t: Throwable) {
            val bg = fromBackground || !AppForeground.isForeground
            val msg = when {
                t is TimeoutCancellationException -> "On-device model timed out"
                bg -> "Waiting for the app to be open (on-device AI runs in the foreground only)"
                else -> t.message?.take(240) ?: t.javaClass.simpleName
            }
            Timber.w(t, "On-device triage failed")
            val updated = events.get(id)!!.copy(aiStatus = if (bg) AiStatus.PENDING else AiStatus.FAILED, aiError = msg)
            events.update(updated)
            return@withContext updated
        }

        val report = verdictValidator.validate(run.rawText)
        val base = events.get(id)!!.copy(
            aiSource = run.source.name, aiModel = run.modelName, aiRaw = report.json.ifBlank { run.rawText }, aiLatencyMs = run.latencyMs,
            schemaValid = report.valid, schemaErrors = report.errors.joinToString("\n").ifBlank { null }, aiError = null,
        )
        val verdict = if (report.valid) runCatching { json.decodeFromString(ScamVerdict.serializer(), report.json) }.getOrNull() else null
        if (verdict == null) {
            // Invalid proposal: discarded entirely. The rules decision stands.
            val rejected = base.copy(aiStatus = AiStatus.REJECTED, schemaValid = false,
                gateNotes = (listOf("Model output failed ${report.schemaId}; proposal discarded, rules decision kept.") + base.gateNotes.lines()).joinToString("\n"))
            events.update(rejected)
            return@withContext rejected
        }
        events.update(base.copy(
            aiStatus = AiStatus.DONE, risk = verdict.riskLevel.name, category = verdict.category.name, confidence = verdict.confidence,
            reasons = verdict.reasons.joinToString("\n"), advice = verdict.advice,
        ))
        // Don't re-run a method the rules already executed for this event.
        val alreadyRan = e.autoApplied && e.finalAction == verdict.method.alias
        if (!alreadyRan) decideAndDispatch(id, MethodProposal(verdict.method, verdict.riskLevel, verdict.confidence, verdict.reasons, run.source))
        val final = events.get(id)!!
        if (notify && !e.isTest && (verdict.riskLevel >= RiskLevel.MEDIUM || final.policyDispatch != Dispatch.DENY.name)) notifier.notifyEvent(final, aiPending = false)
        else if (notify && !e.isTest) notifier.cancel(id)
        final
    }

    suspend fun processPending() {
        if (!AppForeground.isForeground) return
        events.pendingAi().forEach { analyzeOnDevice(it.id) }
    }

    // ---------------------------------------------------------------- Tier 3: cloud (explain_cloud)

    fun redactedPreview(e: ScreeningEventEntity): String = buildString {
        appendLine("Sender: ${Redactor.redactNumber(e.number)}")
        e.body?.let { appendLine(Redactor.redact(it)) }
        if (e.body == null && e.lookupSummary != null) appendLine("Lookup: ${Redactor.redact(e.lookupSummary)}")
    }.trim()
}
