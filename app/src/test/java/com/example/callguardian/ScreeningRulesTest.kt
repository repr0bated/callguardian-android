package com.example.callguardian

import com.example.callguardian.model.Channel
import com.example.callguardian.model.GuardianMethod
import com.example.callguardian.model.RiskLevel
import com.example.callguardian.screening.HeuristicScanner
import com.example.callguardian.screening.Redactor
import com.example.callguardian.screening.ScanInput
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ScreeningRulesTest {
    @Test fun tollScamIsFlagged() {
        val s = HeuristicScanner.scan(ScanInput(Channel.SMS, "+13055550188",
            "E-ZPass FINAL NOTICE: You have an unpaid toll of \$4.15. Pay today to avoid a \$50 late fee: http://ezpass-pay.info/settle"))
        assertTrue(s.riskLevel >= RiskLevel.HIGH)
        assertTrue(s.method == GuardianMethod.QUARANTINE_SMS || s.method == GuardianMethod.BLOCK_NUMBER)
    }

    @Test fun appointmentReminderIsLowRisk() {
        val s = HeuristicScanner.scan(ScanInput(Channel.SMS, "+16175550102", "Reminder: your dental appointment is Tue Dec 1 at 3:30pm. Reply C to confirm.", isContact = true))
        assertTrue(s.riskLevel <= RiskLevel.LOW)
        assertEquals(GuardianMethod.NONE, s.method)
    }

    @Test fun hiddenRepeatCallerIsSuspicious() {
        val s = HeuristicScanner.scan(ScanInput(Channel.CALL, null, callerIdHidden = true, recentCallsFromNumber = 4, lookupSpamScore = 90))
        assertTrue(s.riskLevel >= RiskLevel.MEDIUM)
        assertFalse(s.method == GuardianMethod.QUARANTINE_SMS)
    }

    @Test fun redactorMasksPersonalData() {
        val r = Redactor.redact("Hi Amy, your code is 482913. Email bob@example.com or visit https://bank-secure.top/login/abc?id=99 or call 4155550110")
        assertFalse(r.contains("482913")); assertFalse(r.contains("bob@example.com")); assertFalse(r.contains("4155550110"))
        assertFalse(r.contains("/login/abc")); assertTrue(r.contains("bank-secure.top"))
        assertEquals("••99", Redactor.redactNumber("+15550100199").takeLast(4))
    }
}
