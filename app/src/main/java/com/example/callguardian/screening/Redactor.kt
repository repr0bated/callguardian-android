package com.example.callguardian.screening

/**
 * Strips personal data before anything is sent to the cloud model. Keeps what matters for scam
 * analysis (wording, link domains, amounts, brand names) and masks what identifies the user.
 */
object Redactor {
    private val email = Regex("""[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}""")
    private val url = Regex("""(?i)\b(https?://)?((?:[a-z0-9-]+\.)+[a-z]{2,})(/[^\s]*)?""")
    private val longDigits = Regex("""(?<![\d$])(?:\+?\d[\d\s().-]{6,}\d)(?![\d])""")
    private val codeDigits = Regex("""\b\d{4,8}\b""")
    private val greetingName = Regex("""(?i)\b(hi|hello|hey|dear)\s+([A-Z][a-z]+)""")

    fun redact(text: String): String {
        var t = email.replace(text, "[email]")
        t = url.replace(t) { m ->
            val scheme = m.groupValues[1]
            val host = m.groupValues[2]
            val path = m.groupValues[3]
            // Keep the domain (it's the main phishing signal); drop paths/queries that may carry tokens.
            if (host.count { it == '.' } >= 1 && (scheme.isNotEmpty() || path.isNotEmpty() || host.split('.').last().length in 2..6)) {
                "$scheme$host" + if (path.length > 1) "/[path]" else ""
            } else m.value
        }
        t = longDigits.replace(t, "[number]")
        t = codeDigits.replace(t, "[digits]")
        t = greetingName.replace(t) { "${it.groupValues[1]} [name]" }
        return t
    }

    /** Callers are reduced to country code + last two digits. */
    fun redactNumber(number: String?): String {
        if (number.isNullOrBlank()) return "hidden"
        if (number.contains('@')) return "[email sender]"
        val digits = number.filter { it.isDigit() }
        if (digits.length <= 6) return "short code"
        val cc = if (number.startsWith("+")) "+" + digits.take(if (digits.startsWith("1")) 1 else 2) + " " else ""
        return "$cc•••••${digits.takeLast(2)}"
    }
}
