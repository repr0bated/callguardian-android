package com.example.callguardian.methods

import com.example.callguardian.model.Channel
import com.example.callguardian.model.GuardianMethod
import com.example.callguardian.model.RiskLevel
import com.example.callguardian.model.ScamCategory
import com.example.callguardian.model.label
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/** The trusted facts about a screened event. Models never supply these. */
data class BindingContext(
    val eventId: Long,
    val channel: Channel,
    val number: String?,
    val displayName: String?,
    val risk: RiskLevel,
    val category: ScamCategory,
    val reasons: List<String>,
    val redactedText: String? = null,
    val onDeviceOpinion: String? = null,
)

/**
 * Builds a method's input object. Identifiers (number, event id) come from [BindingContext]; the
 * model only contributes the method choice and its reasons. The result is still validated against
 * the method's input schema before dispatch, so a binding bug can't slip through either.
 */
object MethodBinder {
    fun bind(m: GuardianMethod, c: BindingContext): JsonObject {
        val reason = c.reasons.firstOrNull { it.isNotBlank() }?.take(200) ?: "${c.risk.label()} risk ${c.channel.name.lowercase()}"
        return buildJsonObject {
            when (m) {
                GuardianMethod.BLOCK_NUMBER -> { c.number?.let { put("number", it) }; put("scope", "ALL"); put("reason", reason) }
                GuardianMethod.ALLOW_NUMBER -> { c.number?.let { put("number", it) }; put("reason", reason) }
                GuardianMethod.REJECT_CALL -> {
                    put("number", c.number ?: "hidden")
                    put("mode", if (c.risk >= RiskLevel.CRITICAL) "REJECT" else "SILENCE"); put("reason", reason)
                }
                GuardianMethod.QUARANTINE_SMS -> { put("eventId", c.eventId); put("risk", c.risk.name); put("reason", reason) }
                GuardianMethod.REPORT_SCAM -> {
                    put("eventId", c.eventId); put("channel", c.channel.name); put("category", c.category.label().take(40)); put("reason", reason)
                }
                GuardianMethod.SAVE_CONTACT -> { c.number?.let { put("number", it) }; c.displayName?.let { put("displayName", it.take(80)) }; put("reason", reason) }
                GuardianMethod.EXPLAIN_CLOUD -> {
                    put("eventId", c.eventId); put("channel", c.channel.name); put("redactedText", (c.redactedText ?: "").take(4000))
                    c.onDeviceOpinion?.let { put("onDeviceOpinion", it.take(200)) }
                }
                GuardianMethod.NONE -> {}
            }
        }
    }
}
