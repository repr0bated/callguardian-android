package com.example.callguardian.ai

import com.example.callguardian.model.Channel
import com.example.callguardian.screening.RuleScan

/** Prompts are short on purpose: on-device requests are capped at ~4000 tokens. */
object Prompts {

    fun triage(channel: Channel, senderLabel: String, isContact: Boolean, body: String?, lookup: String?, rules: RuleScan): String = buildString {
        appendLine("You are Call Guardian, a scam detector running privately on the user's phone.")
        appendLine("Classify this incoming ${if (channel == Channel.SMS) "text message" else "phone call"} and pick ONE method to call.")
        appendLine("Methods: NONE (normal), " + (if (channel == Channel.SMS) "QUARANTINE_SMS (hide behind a warning), " else "REJECT_CALL (silence or reject), ") +
            "BLOCK_NUMBER (only for an obvious scam), ALLOW_NUMBER (clearly legitimate), REPORT_SCAM, EXPLAIN_CLOUD (unsure; ask for a deeper look).")
        appendLine("You only propose; a policy checks your choice. Be calm and specific. Reasons must quote or paraphrase the content.")
        appendLine()
        appendLine("Sender: $senderLabel (${if (isContact) "saved contact" else "not in contacts"})")
        if (!lookup.isNullOrBlank()) appendLine("Reverse lookup: $lookup")
        if (rules.flags.isNotEmpty()) appendLine("Local rule hints (may be wrong): ${rules.reasons.joinToString("; ")}")
        if (channel == Channel.SMS) {
            appendLine("Message:")
            appendLine("\"\"\"")
            appendLine(body.orEmpty().take(1500))
            appendLine("\"\"\"")
        } else {
            appendLine("There is no transcript; judge from the caller metadata only.")
        }
        appendLine("Respond with JSON only.")
    }

    fun deep(channel: Channel, redactedSender: String, redactedBody: String?, onDeviceSummary: String?): String = buildString {
        appendLine("You are a fraud analyst helping an ordinary phone user. Personal data has been redacted as [name], [number], [digits], [email], [path].")
        appendLine("Explain clearly whether this ${if (channel == Channel.SMS) "text message" else "phone call"} is a scam, which manipulation tactics it uses, and exactly what the user should do next.")
        appendLine("Never tell the user to tap the link or call the number in the message. Recommend official channels instead.")
        appendLine("Also pick ONE method to offer the user: NONE, BLOCK_NUMBER, ALLOW_NUMBER, REJECT_CALL, QUARANTINE_SMS, REPORT_SCAM or SAVE_CONTACT.")
        appendLine()
        appendLine("Sender: $redactedSender")
        if (!onDeviceSummary.isNullOrBlank()) appendLine("On-device first opinion: $onDeviceSummary")
        if (!redactedBody.isNullOrBlank()) {
            appendLine("Message:")
            appendLine("\"\"\"")
            appendLine(redactedBody.take(4000))
            appendLine("\"\"\"")
        }
        appendLine("Respond with JSON only.")
    }
}
