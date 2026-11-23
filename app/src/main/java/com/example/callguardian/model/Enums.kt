package com.example.callguardian.model

import kotlinx.serialization.Serializable

enum class Channel { CALL, SMS }

/** Ordered from least to most dangerous; ordinal comparisons are used by the policy gate. */
@Serializable
enum class RiskLevel { SAFE, LOW, MEDIUM, HIGH, CRITICAL }

@Serializable
enum class ScamCategory {
    NONE,
    PHISHING_LINK,
    IMPERSONATION,
    PACKAGE_DELIVERY,
    BANK_FRAUD,
    TOLL_OR_FINE,
    PRIZE_OR_LOTTERY,
    JOB_OFFER,
    ROMANCE_OR_WRONG_NUMBER,
    CRYPTO_INVESTMENT,
    TECH_SUPPORT,
    OTP_THEFT,
    ROBOCALL,
    SPOOFED_CALLER,
    OTHER,
}

/**
 * Model-facing names of the typed methods in assets/schemas/registry.json (alias, upper-cased).
 * Firebase AI Logic's @Generable needs a real Kotlin enum here; RegistryConsistencyTest fails the
 * build if this list and the registry ever drift apart.
 */
@Serializable
enum class GuardianMethod {
    NONE, BLOCK_NUMBER, ALLOW_NUMBER, REJECT_CALL, QUARANTINE_SMS, REPORT_SCAM, SAVE_CONTACT, EXPLAIN_CLOUD;

    val alias: String get() = name.lowercase()

    companion object {
        fun fromAlias(alias: String?): GuardianMethod = entries.firstOrNull { it.alias == alias } ?: NONE
    }
}

/** Where a verdict came from. */
enum class VerdictSource { RULES, ON_DEVICE, IN_CLOUD, USER }

fun ScamCategory.label(): String = when (this) {
    ScamCategory.NONE -> "No scam pattern"
    ScamCategory.PHISHING_LINK -> "Phishing link"
    ScamCategory.IMPERSONATION -> "Impersonation"
    ScamCategory.PACKAGE_DELIVERY -> "Fake delivery"
    ScamCategory.BANK_FRAUD -> "Bank fraud"
    ScamCategory.TOLL_OR_FINE -> "Fake toll / fine"
    ScamCategory.PRIZE_OR_LOTTERY -> "Prize / lottery"
    ScamCategory.JOB_OFFER -> "Job scam"
    ScamCategory.ROMANCE_OR_WRONG_NUMBER -> "Wrong-number opener"
    ScamCategory.CRYPTO_INVESTMENT -> "Crypto / investment"
    ScamCategory.TECH_SUPPORT -> "Tech support"
    ScamCategory.OTP_THEFT -> "Code theft"
    ScamCategory.ROBOCALL -> "Robocall"
    ScamCategory.SPOOFED_CALLER -> "Spoofed caller"
    ScamCategory.OTHER -> "Other"
}

fun GuardianMethod.label(): String = when (this) {
    GuardianMethod.NONE -> "No action"
    GuardianMethod.BLOCK_NUMBER -> "Block sender"
    GuardianMethod.ALLOW_NUMBER -> "Trust sender"
    GuardianMethod.REJECT_CALL -> "Silence call"
    GuardianMethod.QUARANTINE_SMS -> "Quarantine"
    GuardianMethod.REPORT_SCAM -> "Report scam"
    GuardianMethod.SAVE_CONTACT -> "Save contact"
    GuardianMethod.EXPLAIN_CLOUD -> "Explain (cloud)"
}

fun RiskLevel.label(): String = name.lowercase().replaceFirstChar { it.uppercase() }
