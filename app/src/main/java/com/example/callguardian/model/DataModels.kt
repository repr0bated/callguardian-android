package com.example.callguardian.model

import com.example.callguardian.service.ContactInfo

data class LookupResult(
    val displayName: String?,
    val carrier: String?,
    val region: String?,
    val tags: List<String>,
    val source: String,
    val lineType: String?,
    val spamScore: Int?,
    val summary: String?
)

data class AiAssessment(
    val label: String,
    val confidencePercent: Int
)

enum class BlockMode {
    NONE,
    CALLS,
    MESSAGES,
    ALL
}

enum class InteractionType {
    CALL,
    SMS
}

enum class InteractionDirection {
    INCOMING,
    OUTGOING,
    MISSED
}

data class LookupOutcome(
    val contactInfo: ContactInfo?,
    val lookupResult: LookupResult?
)

class CallResponse {}
