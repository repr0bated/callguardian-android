package com.example.callguardian.model

data class LookupOutcome(
    val lookupResult: LookupResult?,
    val aiAssessment: AiAssessment?,
    val contactInfo: ContactInfo?
)
