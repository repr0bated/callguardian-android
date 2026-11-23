package com.example.callguardian.methods

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import com.example.callguardian.ai.DeepAnalysis
import com.example.callguardian.ai.HybridScamAnalyzer
import com.example.callguardian.ai.Prompts
import com.example.callguardian.data.db.AiStatus
import com.example.callguardian.data.db.PhoneProfileDao
import com.example.callguardian.data.db.PhoneProfileEntity
import com.example.callguardian.data.db.ScreeningEventDao
import com.example.callguardian.methods.gen.GuardianCallAllowV1Input
import com.example.callguardian.methods.gen.GuardianCallAllowV1Output
import com.example.callguardian.methods.gen.GuardianCallBlockV1Input
import com.example.callguardian.methods.gen.GuardianCallBlockV1Output
import com.example.callguardian.methods.gen.GuardianCallRejectV1Input
import com.example.callguardian.methods.gen.GuardianCallRejectV1Output
import com.example.callguardian.methods.gen.GuardianCloudExplainV1Input
import com.example.callguardian.methods.gen.GuardianCloudExplainV1Output
import com.example.callguardian.methods.gen.GuardianContactSaveV1Input
import com.example.callguardian.methods.gen.GuardianContactSaveV1Output
import com.example.callguardian.methods.gen.GuardianReportScamV1Input
import com.example.callguardian.methods.gen.GuardianReportScamV1Output
import com.example.callguardian.methods.gen.GuardianSmsQuarantineV1Input
import com.example.callguardian.methods.gen.GuardianSmsQuarantineV1Output
import com.example.callguardian.model.BlockMode
import com.example.callguardian.model.Channel
import com.example.callguardian.notifications.AlertNotifier
import com.example.callguardian.policy.SchemaValidator
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import javax.inject.Inject
import javax.inject.Singleton

data class HandlerExtras(val respondedWithinMs: Long = 0)

data class HandlerOutcome(val output: JsonElement, val launch: Intent? = null, val notes: List<String> = emptyList())

/**
 * Method bodies. Each one takes the *generated* input type and returns the *generated* output type
 * (see build/generated/source/schemas). Nothing here is reachable except through [MethodDispatcher].
 */
@Singleton
class MethodHandlers @Inject constructor(
    @ApplicationContext private val context: Context,
    private val profiles: PhoneProfileDao,
    private val events: ScreeningEventDao,
    private val notifier: AlertNotifier,
    private val analyzer: HybridScamAnalyzer,
) {
    private val json = MethodRegistry.json
    private val deepValidator by lazy {
        SchemaValidator(context.assets.open("schemas/deep_analysis.schema.json").bufferedReader().use { it.readText() }, "deep_analysis.schema.json")
    }

    private fun <I> decode(s: KSerializer<I>, input: JsonObject): I = json.decodeFromJsonElement(s, input)
    private fun <O> out(s: KSerializer<O>, o: O): JsonElement = json.encodeToJsonElement(s, o)

    suspend fun execute(m: com.example.callguardian.model.GuardianMethod, input: JsonObject, extras: HandlerExtras): HandlerOutcome = when (m) {
        com.example.callguardian.model.GuardianMethod.BLOCK_NUMBER -> {
            val i = decode(GuardianCallBlockV1Input.serializer(), input)
            val prev = profiles.get(i.number)
            val mode = when (i.scope) {
                GuardianCallBlockV1Input.Scope.CALLS -> BlockMode.CALLS
                GuardianCallBlockV1Input.Scope.MESSAGES -> BlockMode.MESSAGES
                GuardianCallBlockV1Input.Scope.ALL -> BlockMode.ALL
            }
            val now = System.currentTimeMillis()
            profiles.upsert((prev ?: PhoneProfileEntity(phoneNumber = i.number)).copy(blockMode = mode.name, trusted = false, updatedAt = now))
            HandlerOutcome(out(GuardianCallBlockV1Output.serializer(), GuardianCallBlockV1Output(
                number = i.number, scope = GuardianCallBlockV1Output.Scope.valueOf(i.scope.name),
                previouslyBlocked = prev?.mode != null && prev.mode != BlockMode.NONE, blockedAt = now)))
        }
        com.example.callguardian.model.GuardianMethod.ALLOW_NUMBER -> {
            val i = decode(GuardianCallAllowV1Input.serializer(), input)
            val prev = profiles.get(i.number)
            profiles.upsert((prev ?: PhoneProfileEntity(phoneNumber = i.number)).copy(blockMode = BlockMode.NONE.name, trusted = true, updatedAt = System.currentTimeMillis()))
            HandlerOutcome(out(GuardianCallAllowV1Output.serializer(), GuardianCallAllowV1Output(i.number, true, prev?.mode?.let { it != BlockMode.NONE } ?: false)))
        }
        com.example.callguardian.model.GuardianMethod.REJECT_CALL -> {
            val i = decode(GuardianCallRejectV1Input.serializer(), input)
            // The CallScreeningService applies the response using this output's mode.
            HandlerOutcome(out(GuardianCallRejectV1Output.serializer(), GuardianCallRejectV1Output(
                GuardianCallRejectV1Output.Mode.valueOf(i.mode.name), true, extras.respondedWithinMs)))
        }
        com.example.callguardian.model.GuardianMethod.QUARANTINE_SMS -> {
            val i = decode(GuardianSmsQuarantineV1Input.serializer(), input)
            val e = events.get(i.eventId) ?: error("Event ${i.eventId} not found")
            val updated = e.copy(quarantined = true)
            events.update(updated)
            val posted = if (!e.isTest) { notifier.notifyEvent(updated, aiPending = false); true } else false
            HandlerOutcome(out(GuardianSmsQuarantineV1Output.serializer(), GuardianSmsQuarantineV1Output(i.eventId, true, posted)),
                notes = if (e.isTest) listOf("Test event: no notification posted.") else emptyList())
        }
        com.example.callguardian.model.GuardianMethod.REPORT_SCAM -> {
            val i = decode(GuardianReportScamV1Input.serializer(), input)
            val e = events.get(i.eventId) ?: error("Event ${i.eventId} not found")
            val body = if (i.channel == GuardianReportScamV1Input.Channel.SMS) e.body.orEmpty() else "Spam call from ${e.number ?: "hidden number"}"
            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:7726")).putExtra("sms_body", body).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            HandlerOutcome(out(GuardianReportScamV1Output.serializer(), GuardianReportScamV1Output(i.eventId, "7726 (SPAM)", true)), launch = intent)
        }
        com.example.callguardian.model.GuardianMethod.SAVE_CONTACT -> {
            val i = decode(GuardianContactSaveV1Input.serializer(), input)
            val intent = Intent(ContactsContract.Intents.Insert.ACTION).setType(ContactsContract.RawContacts.CONTENT_TYPE)
                .putExtra(ContactsContract.Intents.Insert.PHONE, i.number)
                .apply { i.displayName?.let { putExtra(ContactsContract.Intents.Insert.NAME, it) } }
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            HandlerOutcome(out(GuardianContactSaveV1Output.serializer(), GuardianContactSaveV1Output(i.number, true)), launch = intent)
        }
        com.example.callguardian.model.GuardianMethod.EXPLAIN_CLOUD -> explainCloud(decode(GuardianCloudExplainV1Input.serializer(), input))
        com.example.callguardian.model.GuardianMethod.NONE -> error("NONE is not dispatchable")
    }

    private suspend fun explainCloud(i: GuardianCloudExplainV1Input): HandlerOutcome {
        val e = events.get(i.eventId) ?: error("Event ${i.eventId} not found")
        events.update(e.copy(deepStatus = AiStatus.RUNNING, deepRedactedInput = i.redactedText, deepError = null))
        val channel = Channel.valueOf(i.channel.name)
        val sender = i.redactedText.lineSequence().firstOrNull()?.removePrefix("Sender: ") ?: "unknown"
        val body = i.redactedText.lines().drop(1).joinToString("\n").ifBlank { null }
        val run = try {
            withTimeout(30_000) { analyzer.deepAnalysis(Prompts.deep(channel, sender, body, i.onDeviceOpinion)) }
        } catch (t: Throwable) {
            val msg = t.message?.let { m -> if (m.contains("App Check", true) || m.contains("403")) "App Check rejected the request. Register this device's debug token in the Firebase console." else m.take(240) } ?: t.javaClass.simpleName
            events.get(i.eventId)?.let { events.update(it.copy(deepStatus = AiStatus.FAILED, deepError = msg)) }
            throw IllegalStateException(msg, t)
        }
        val report = deepValidator.validate(run.rawText)
        val cur = events.get(i.eventId) ?: e
        if (!report.valid) {
            events.update(cur.copy(deepStatus = AiStatus.REJECTED, deepRaw = report.json, deepSchemaValid = false, deepLatencyMs = run.latencyMs,
                deepError = report.errors.joinToString("\n")))
            error("Gemini output failed ${report.schemaId}: ${report.errors.joinToString("; ")}")
        }
        val d = json.decodeFromString(DeepAnalysis.serializer(), report.json)
        events.update(cur.copy(deepStatus = AiStatus.DONE, deepRaw = report.json, deepSchemaValid = true, deepLatencyMs = run.latencyMs, deepError = null))
        return HandlerOutcome(out(GuardianCloudExplainV1Output.serializer(), GuardianCloudExplainV1Output(
            eventId = i.eventId, riskLevel = GuardianCloudExplainV1Output.RiskLevel.valueOf(d.riskLevel.name),
            category = d.category.name.take(40), confidence = d.confidence.coerceIn(0, 100), summary = d.summary.take(1200),
            tactics = d.tactics.take(5).map { it.take(300) }, whatToDo = d.whatToDo.take(5).map { it.take(300) },
            proposedMethod = d.method.alias, model = (run.modelName ?: "gemini").take(64), latencyMs = run.latencyMs)),
            notes = listOf("Gemini output passed ${report.schemaId}."))
    }
}
