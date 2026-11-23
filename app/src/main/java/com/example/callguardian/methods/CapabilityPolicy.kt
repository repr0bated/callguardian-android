package com.example.callguardian.methods

import com.example.callguardian.methods.gen.Registry
import com.example.callguardian.model.Channel
import com.example.callguardian.model.GuardianMethod
import com.example.callguardian.model.RiskLevel
import com.example.callguardian.model.VerdictSource

/** What a proposer (rules, on-device model, cloud model, or the user) asked for. */
data class MethodProposal(
    val method: GuardianMethod,
    val riskLevel: RiskLevel,
    val confidence: Int,
    val reasons: List<String>,
    val source: VerdictSource,
)

data class PolicyContext(
    val channel: Channel,
    val isContact: Boolean,
    val isTrusted: Boolean,
    val autoBlockEnabled: Boolean = true,
    val autoBlockThreshold: Int = 85,
    val cloudAvailable: Boolean = true,
)

enum class Dispatch {
    /** Run now, no tap needed. */
    AUTO,
    /** Offer as a one-tap action with a schema-rendered confirmation sheet. */
    OFFER,
    /** Refused by policy; recorded in a receipt, never run. */
    DENY,
}

data class PolicyDecision(
    val proposed: GuardianMethod,
    /** The method that will actually be auto-run or offered (may be a safer fallback). */
    val method: GuardianMethod,
    val dispatch: Dispatch,
    val notes: List<String>,
) {
    val substituted: Boolean get() = method != proposed
}

/**
 * Capability policy over the method registry. A model can only *propose* a registry method; this
 * decides whether it may run, needs a tap, or is denied — based on the method's declared
 * capability, channels, risk/confidence bounds, contact safety and execution (device|cloud).
 * Pure Kotlin; see CapabilityPolicyTest.
 */
class CapabilityPolicy(private val registry: MethodRegistry) {

    fun decide(p: MethodProposal, ctx: PolicyContext): PolicyDecision {
        val notes = mutableListOf<String>()
        var method = p.method

        // Contradiction: high risk but nothing proposed -> safest useful method for the channel.
        if (method == GuardianMethod.NONE && p.riskLevel >= RiskLevel.HIGH && !ctx.isContact && !ctx.isTrusted) {
            method = if (ctx.channel == Channel.SMS) GuardianMethod.QUARANTINE_SMS else GuardianMethod.REJECT_CALL
            notes += "Risk is ${p.riskLevel} but no method was proposed; using ${method.alias}."
        }
        if (method == GuardianMethod.NONE) return PolicyDecision(p.method, method, Dispatch.DENY, notes + "No method proposed.")

        var decision = check(method, p, ctx, notes)
        // Safer fallback when the strongest method isn't allowed.
        if (decision == Dispatch.DENY && method == GuardianMethod.BLOCK_NUMBER && !ctx.isContact && !ctx.isTrusted) {
            val fb = if (ctx.channel == Channel.SMS) GuardianMethod.QUARANTINE_SMS else GuardianMethod.REJECT_CALL
            val fbNotes = mutableListOf<String>()
            val fbDecision = check(fb, p, ctx, fbNotes)
            if (fbDecision != Dispatch.DENY) {
                notes += "Falling back to ${fb.alias}."
                notes += fbNotes
                method = fb; decision = fbDecision
            }
        }
        if (notes.isEmpty()) notes += "Allowed by policy."
        return PolicyDecision(p.method, method, decision, notes)
    }

    private fun check(m: GuardianMethod, p: MethodProposal, ctx: PolicyContext, notes: MutableList<String>): Dispatch {
        val reg = registry.byMethod(m) ?: run { notes += "${m.alias} is not in registry ${registry.version}."; return Dispatch.DENY }
        val spec = reg.spec
        val tag = spec.id
        if (spec.channels.none { it.name == ctx.channel.name }) { notes += "$tag doesn't apply to ${ctx.channel}."; return Dispatch.DENY }
        val minRisk = RiskLevel.valueOf(spec.minRisk.name)
        if (p.riskLevel < minRisk) { notes += "$tag needs risk ≥ $minRisk (got ${p.riskLevel})."; return Dispatch.DENY }
        spec.maxRisk?.let { mx -> if (p.riskLevel > RiskLevel.valueOf(mx.name)) { notes += "$tag only allowed up to risk ${mx.name} (got ${p.riskLevel})."; return Dispatch.DENY } }
        val minConf = if (m == GuardianMethod.BLOCK_NUMBER) maxOf(spec.minConfidence ?: 0, ctx.autoBlockThreshold) else spec.minConfidence ?: 0
        if (p.source != VerdictSource.RULES && p.confidence < minConf) {
            notes += "$tag needs confidence ≥ $minConf (got ${p.confidence})."; return Dispatch.DENY
        }
        if (!spec.contactSafe && (ctx.isContact || ctx.isTrusted)) {
            notes += "$tag never runs against ${if (ctx.isTrusted) "a trusted sender" else "a saved contact"}."; return Dispatch.DENY
        }
        if (spec.capability == "telephony.block" && p.reasons.none { it.isNotBlank() }) { notes += "$tag needs at least one reason."; return Dispatch.DENY }
        if (spec.execution == Registry.Execution.cloud && !ctx.cloudAvailable) { notes += "$tag runs in the cloud, which isn't available."; return Dispatch.DENY }

        // Who may act without a tap.
        return when {
            p.source == VerdictSource.IN_CLOUD -> { notes += "Cloud proposals are offered, never auto-run."; Dispatch.OFFER }
            spec.requiresConsent == true -> { notes += "$tag needs your consent."; Dispatch.OFFER }
            !spec.autoApply -> { notes += "$tag is user-confirmed by design."; Dispatch.OFFER }
            m == GuardianMethod.BLOCK_NUMBER && !ctx.autoBlockEnabled -> { notes += "Auto-block is off; offered instead."; Dispatch.OFFER }
            else -> Dispatch.AUTO
        }
    }
}
