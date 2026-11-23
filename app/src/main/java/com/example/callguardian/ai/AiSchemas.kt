package com.example.callguardian.ai

import com.example.callguardian.model.GuardianMethod
import com.example.callguardian.model.RiskLevel
import com.example.callguardian.model.ScamCategory
import com.google.firebase.ai.annotations.Generable
import com.google.firebase.ai.annotations.Guide
import kotlinx.serialization.Serializable

/**
 * Structured output for the fast, private triage pass (Gemma 4 / Gemini Nano 4 on-device via
 * Firebase AI Logic). The KSP processor turns this into a Firebase `JsonSchema` that is used as an
 * on-device decoding constraint and, on cloud fallback, as the Gemini `responseSchema`.
 *
 * Mirrors assets/schemas/scam_verdict.schema.json, which every raw model response is validated
 * against before the policy gate is allowed to act on it.
 */
@Serializable
@Generable(description = "Scam triage verdict for one incoming SMS or phone call")
data class ScamVerdict(
    @Guide(description = "Overall risk to the user")
    val riskLevel: RiskLevel,
    @Guide(description = "Most likely scam type, or NONE if the content looks benign")
    val category: ScamCategory,
    @Guide(description = "Confidence in this verdict from 0 to 100", minimum = 0.0, maximum = 100.0)
    val confidence: Int,
    @Guide(description = "Up to 3 short, concrete red flags taken from the content", maxItems = 3)
    val reasons: List<String>,
    @Guide(description = "The ONE typed method to call: BLOCK_NUMBER (obvious scam), QUARANTINE_SMS (suspicious text), REJECT_CALL (suspicious call), REPORT_SCAM, ALLOW_NUMBER or SAVE_CONTACT (clearly legitimate), EXPLAIN_CLOUD (unsure, needs a deeper look), or NONE")
    val method: GuardianMethod,
    @Guide(description = "One short sentence of advice for the user")
    val advice: String,
) {
    companion object
}

/** Structured output for the opt-in cloud deep analysis (Gemini, ONLY_IN_CLOUD, redacted input). */
@Serializable
@Generable(description = "Detailed explanation of a suspicious message or call")
data class DeepAnalysis(
    @Guide(description = "Overall risk to the user")
    val riskLevel: RiskLevel,
    @Guide(description = "Most likely scam type, or NONE if benign")
    val category: ScamCategory,
    @Guide(description = "Confidence from 0 to 100", minimum = 0.0, maximum = 100.0)
    val confidence: Int,
    @Guide(description = "Two or three sentence plain-English explanation of what is going on")
    val summary: String,
    @Guide(description = "Manipulation tactics the sender is using", maxItems = 5)
    val tactics: List<String>,
    @Guide(description = "Concrete next steps for the user", maxItems = 5)
    val whatToDo: List<String>,
    @Guide(description = "The ONE typed method to call next (never EXPLAIN_CLOUD here), or NONE")
    val method: GuardianMethod,
) {
    companion object
}
