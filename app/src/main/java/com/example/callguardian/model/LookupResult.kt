package com.example.callguardian.model

data class LookupResult(
    val phoneNumber: String,
    val displayName: String? = null,
    val carrier: String? = null,
    val region: String? = null,
    val lineType: String? = null,
    val spamScore: Int? = null,
    val tags: List<String> = emptyList(),
    val source: String,
    val rawPayload: Map<String, Any?> = emptyMap()
) {
    val summary: String?
        get() = displayName ?: tags.firstOrNull() ?: carrier ?: region
}
