package com.example.callguardian.screening

import com.example.callguardian.model.Channel
import com.example.callguardian.model.GuardianMethod
import com.example.callguardian.model.RiskLevel
import com.example.callguardian.model.ScamCategory

data class RuleFlag(val id: String, val label: String, val weight: Int, val category: ScamCategory? = null)

data class RuleScan(
    val score: Int,
    val flags: List<RuleFlag>,
    val riskLevel: RiskLevel,
    val category: ScamCategory,
    /** Registry method the rule engine proposes (goes through the same policy as model output). */
    val method: GuardianMethod,
    val elapsedMicros: Long,
) {
    val reasons: List<String> get() = flags.sortedByDescending { it.weight }.map { it.label }
}

/** Everything the rule engine may look at. Lookup fields are optional (no API keys = null). */
data class ScanInput(
    val channel: Channel,
    val number: String?,
    val body: String? = null,
    val isContact: Boolean = false,
    val callerIdHidden: Boolean = false,
    val lookupSpamScore: Int? = null,
    val lookupLineType: String? = null,
    val lookupTags: List<String> = emptyList(),
    val recentCallsFromNumber: Int = 0,
)

/**
 * Instant, offline, deterministic first pass. Runs on every call and SMS (including inside the
 * 5-second CallScreeningService budget and while the app is in the background, where on-device
 * generative inference is not allowed). Its flags are also fed to the on-device model as hints.
 */
object HeuristicScanner {

    private val urlRegex = Regex("""(?i)\b((?:https?://|www\.)[^\s]+|[a-z0-9-]+(?:\.[a-z0-9-]+)*\.(?:com|net|org|info|top|xyz|icu|click|live|cc|vip|shop|buzz|link|online|site|me|co|us|io|ly|gd|gl)(?:/[^\s]*)?)""")
    private val ipUrlRegex = Regex("""(?i)https?://\d{1,3}(?:\.\d{1,3}){3}""")
    private val shorteners = listOf("bit.ly", "tinyurl", "t.co/", "is.gd", "rb.gy", "cutt.ly", "shorturl", "goo.gl", "ow.ly", "tiny.cc", "s.id/")
    private val riskyTlds = listOf(".top", ".xyz", ".icu", ".click", ".live", ".cc", ".vip", ".shop", ".buzz", ".link", ".online", ".site", ".info")
    private val brands = listOf(
        "usps", "fedex", "ups", "dhl", "amazon", "apple", "icloud", "paypal", "netflix", "chase",
        "wells fargo", "bank of america", "citi", "irs", "medicare", "social security", "dmv",
        "e-zpass", "ezpass", "fastrak", "sunpass", "coinbase", "venmo", "zelle", "microsoft",
    )

    private data class Pattern(val id: String, val label: String, val weight: Int, val category: ScamCategory?, val regex: Regex)

    private val textPatterns = listOf(
        Pattern("urgency", "Pressure to act fast", 15, null,
            Regex("""(?i)\b(urgent|immediately|final (notice|warning)|within (24|48) ?h(ours)?|act now|suspend(ed)?|locked|expire[sd]?|last chance|today only)\b""")),
        Pattern("money", "Asks for gift cards, wire or crypto", 20, ScamCategory.CRYPTO_INVESTMENT,
            Regex("""(?i)\b(gift ?cards?|wire transfer|western union|moneygram|bitcoin|btc|usdt|crypto|cash ?app|zelle me|venmo me)\b""")),
        Pattern("otp", "Asks you to share a code or password", 25, ScamCategory.OTP_THEFT,
            Regex("""(?i)(\b(send|share|reply with|tell me|forward)\b.{0,30}\b(code|otp|pin|password|passcode)\b)|(\b(code|otp)\b.{0,40}\b(send it|share it|reply)\b)""")),
        Pattern("credentials", "Asks to verify account or personal info", 15, ScamCategory.BANK_FRAUD,
            Regex("""(?i)\b(verify (your )?(account|identity|information)|confirm (your )?(details|identity|card)|update (your )?(billing|payment)|ssn|social security number)\b""")),
        Pattern("delivery", "Fake delivery / redelivery story", 15, ScamCategory.PACKAGE_DELIVERY,
            Regex("""(?i)\b(package|parcel|shipment|delivery|redeliver(y)?)\b.{0,60}\b(failed|on hold|unable|pending|fee|address|reschedule)\b""")),
        Pattern("toll", "Unpaid toll or fine", 20, ScamCategory.TOLL_OR_FINE,
            Regex("""(?i)\b(unpaid|outstanding|overdue)\b.{0,30}\b(toll|fine|balance|invoice|ticket)\b|\btoll (services|balance)\b""")),
        Pattern("prize", "Claims you won something", 15, ScamCategory.PRIZE_OR_LOTTERY,
            Regex("""(?i)\b(you('ve| have)? won|winner|congratulations|claim (your )?(prize|reward|gift)|lottery|sweepstakes)\b""")),
        Pattern("job", "Too-good-to-be-true job offer", 15, ScamCategory.JOB_OFFER,
            Regex("""(?i)\b(remote (job|position|work)|part[- ]time|recruit(er|ing)|hiring)\b.{0,80}(\$\s?\d{2,4}|per (day|hour)|daily pay)""")),
        Pattern("wrongnumber", "Wrong-number style opener", 10, ScamCategory.ROMANCE_OR_WRONG_NUMBER,
            Regex("""(?i)^(hi|hello|hey)?[,! ]*(is this|are you) [a-z]+\??|\b(wrong number|long time no see|do you remember me)\b""")),
        Pattern("techsupport", "Fake security / tech-support alert", 15, ScamCategory.TECH_SUPPORT,
            Regex("""(?i)\b(virus|hacked|unauthori[sz]ed (login|access|purchase|charge)|suspicious (activity|login|sign-?in)|fraud alert)\b""")),
    )

    fun scan(input: ScanInput): RuleScan {
        val start = System.nanoTime()
        val flags = mutableListOf<RuleFlag>()
        val body = input.body.orEmpty()
        val lower = body.lowercase()

        if (input.channel == Channel.SMS && body.isNotBlank()) {
            val urls = urlRegex.findAll(body).map { it.value.lowercase() }.toList()
            if (urls.isNotEmpty()) {
                flags += RuleFlag("link", "Contains a link", 15, ScamCategory.PHISHING_LINK)
                if (urls.any { u -> shorteners.any { u.contains(it) } }) flags += RuleFlag("shortener", "Link hidden behind a URL shortener", 15, ScamCategory.PHISHING_LINK)
                if (ipUrlRegex.containsMatchIn(body)) flags += RuleFlag("iplink", "Link points to a bare IP address", 20, ScamCategory.PHISHING_LINK)
                if (urls.any { u -> riskyTlds.any { tld -> hostOf(u).endsWith(tld) } }) flags += RuleFlag("tld", "Link uses a throwaway domain", 15, ScamCategory.PHISHING_LINK)
                val lookalike = urls.firstOrNull { u -> val h = hostOf(u); brands.any { b -> val bb = b.replace(" ", ""); h.contains(bb) && !isOfficial(h, bb) } }
                if (lookalike != null) flags += RuleFlag("lookalike", "Look-alike brand domain (${hostOf(lookalike)})", 25, ScamCategory.IMPERSONATION)
            }
            textPatterns.forEach { p -> if (p.regex.containsMatchIn(body)) flags += RuleFlag(p.id, p.label, p.weight, p.category) }
            val brand = brands.firstOrNull { Regex("""\b${Regex.escape(it)}\b""").containsMatchIn(lower) }
            if (brand != null && flags.any { it.weight >= 15 }) {
                flags += RuleFlag("brand", "Uses the name \"${brand.uppercase()}\"", 10, ScamCategory.IMPERSONATION)
            }
            if (input.number?.contains('@') == true) flags += RuleFlag("email2sms", "Sent from an email address, not a phone", 15)
        }

        if (input.channel == Channel.CALL) {
            if (input.callerIdHidden || input.number.isNullOrBlank()) flags += RuleFlag("hidden", "Caller ID is hidden", 20, ScamCategory.SPOOFED_CALLER)
            input.lookupSpamScore?.let { s ->
                if (s >= 70) flags += RuleFlag("lookupspam", "Reverse lookup spam score $s", 50, ScamCategory.ROBOCALL)
                else if (s >= 40) flags += RuleFlag("lookupspam", "Reverse lookup spam score $s", 25, ScamCategory.ROBOCALL)
            }
            if (input.lookupTags.any { it.contains("spam", true) || it.contains("scam", true) || it.contains("fraud", true) }) {
                flags += RuleFlag("lookuptag", "Reported as spam by lookup service", 40, ScamCategory.ROBOCALL)
            }
            if (input.lookupLineType?.contains("voip", true) == true) flags += RuleFlag("voip", "VoIP number (easy to spoof)", 10, ScamCategory.SPOOFED_CALLER)
            if (input.recentCallsFromNumber >= 2) flags += RuleFlag("repeat", "Called ${input.recentCallsFromNumber + 1} times in 15 minutes", 15, ScamCategory.ROBOCALL)
        }

        if (!input.isContact && input.number != null && flags.isNotEmpty()) {
            flags += RuleFlag("unknown", "Not in your contacts", 5)
        }

        var score = flags.sumOf { it.weight }.coerceIn(0, 100)
        if (input.isContact) score = (score / 2)
        val risk = when {
            score >= 80 -> RiskLevel.CRITICAL
            score >= 60 -> RiskLevel.HIGH
            score >= 35 -> RiskLevel.MEDIUM
            score >= 15 -> RiskLevel.LOW
            else -> RiskLevel.SAFE
        }
        val category = flags.filter { it.category != null }.maxByOrNull { it.weight }?.category ?: ScamCategory.NONE
        val method = when {
            risk == RiskLevel.CRITICAL -> GuardianMethod.BLOCK_NUMBER
            risk == RiskLevel.HIGH && input.channel == Channel.CALL -> GuardianMethod.REJECT_CALL
            risk >= RiskLevel.MEDIUM && input.channel == Channel.SMS -> GuardianMethod.QUARANTINE_SMS
            else -> GuardianMethod.NONE
        }
        return RuleScan(score, flags, risk, category, method, (System.nanoTime() - start) / 1000)
    }

    internal fun hostOf(url: String): String =
        url.removePrefix("https://").removePrefix("http://").removePrefix("www.")
            .substringBefore('/').substringBefore('?').substringBefore(':').trimEnd('.', ',', ')')

    private fun isOfficial(host: String, brand: String): Boolean {
        val official = mapOf(
            "usps" to "usps.com", "fedex" to "fedex.com", "ups" to "ups.com", "dhl" to "dhl.com",
            "amazon" to "amazon.com", "apple" to "apple.com", "icloud" to "icloud.com", "paypal" to "paypal.com",
            "netflix" to "netflix.com", "chase" to "chase.com", "wellsfargo" to "wellsfargo.com",
            "bankofamerica" to "bankofamerica.com", "citi" to "citi.com", "irs" to "irs.gov",
            "medicare" to "medicare.gov", "socialsecurity" to "ssa.gov", "ezpass" to "e-zpassny.com",
            "coinbase" to "coinbase.com", "venmo" to "venmo.com", "zelle" to "zellepay.com", "microsoft" to "microsoft.com",
        )[brand] ?: return false
        return host == official || host.endsWith(".$official")
    }
}
