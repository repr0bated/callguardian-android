package com.example.callguardian

import com.example.callguardian.methods.CapabilityPolicy
import com.example.callguardian.methods.Dispatch
import com.example.callguardian.methods.MethodProposal
import com.example.callguardian.methods.PolicyContext
import com.example.callguardian.model.Channel
import com.example.callguardian.model.GuardianMethod
import com.example.callguardian.model.RiskLevel
import com.example.callguardian.model.VerdictSource
import org.junit.Assert.assertEquals
import org.junit.Test

class CapabilityPolicyTest {
    private val policy = CapabilityPolicy(TestAssets.registry)
    private val sms = PolicyContext(Channel.SMS, isContact = false, isTrusted = false)
    private fun p(m: GuardianMethod, r: RiskLevel, c: Int = 95, src: VerdictSource = VerdictSource.ON_DEVICE, reasons: List<String> = listOf("Look-alike link")) =
        MethodProposal(m, r, c, reasons, src)

    @Test fun confidentOnDeviceBlockAutoRuns() {
        val d = policy.decide(p(GuardianMethod.BLOCK_NUMBER, RiskLevel.CRITICAL), sms)
        assertEquals(GuardianMethod.BLOCK_NUMBER, d.method); assertEquals(Dispatch.AUTO, d.dispatch)
    }

    @Test fun lowConfidenceBlockFallsBackToQuarantine() {
        val d = policy.decide(p(GuardianMethod.BLOCK_NUMBER, RiskLevel.HIGH, c = 60), sms)
        assertEquals(GuardianMethod.QUARANTINE_SMS, d.method); assertEquals(Dispatch.AUTO, d.dispatch)
    }

    @Test fun blockOnCallFallsBackToReject() {
        val d = policy.decide(p(GuardianMethod.BLOCK_NUMBER, RiskLevel.HIGH, c = 60), sms.copy(channel = Channel.CALL))
        assertEquals(GuardianMethod.REJECT_CALL, d.method)
    }

    @Test fun neverBlocksContacts() {
        val d = policy.decide(p(GuardianMethod.BLOCK_NUMBER, RiskLevel.CRITICAL), sms.copy(isContact = true))
        assertEquals(Dispatch.DENY, d.dispatch)
    }

    @Test fun userThresholdRaisesBlockBar() {
        val d = policy.decide(p(GuardianMethod.BLOCK_NUMBER, RiskLevel.CRITICAL, c = 90), sms.copy(autoBlockThreshold = 95))
        assertEquals(GuardianMethod.QUARANTINE_SMS, d.method)
    }

    @Test fun autoBlockOffMeansOffer() {
        val d = policy.decide(p(GuardianMethod.BLOCK_NUMBER, RiskLevel.CRITICAL), sms.copy(autoBlockEnabled = false))
        assertEquals(Dispatch.OFFER, d.dispatch)
    }

    @Test fun cloudProposalsAreOnlyOffered() {
        val d = policy.decide(p(GuardianMethod.BLOCK_NUMBER, RiskLevel.CRITICAL, src = VerdictSource.IN_CLOUD), sms)
        assertEquals(Dispatch.OFFER, d.dispatch)
    }

    @Test fun rejectCallNotAllowedOnSms() {
        val d = policy.decide(p(GuardianMethod.REJECT_CALL, RiskLevel.HIGH), sms)
        assertEquals(Dispatch.DENY, d.dispatch)
    }

    @Test fun allowNumberOnlyForLowRiskAndNeedsTap() {
        assertEquals(Dispatch.DENY, policy.decide(p(GuardianMethod.ALLOW_NUMBER, RiskLevel.HIGH), sms).dispatch)
        assertEquals(Dispatch.OFFER, policy.decide(p(GuardianMethod.ALLOW_NUMBER, RiskLevel.SAFE), sms).dispatch)
    }

    @Test fun explainCloudNeedsConsentAndCloud() {
        assertEquals(Dispatch.OFFER, policy.decide(p(GuardianMethod.EXPLAIN_CLOUD, RiskLevel.MEDIUM), sms).dispatch)
        assertEquals(Dispatch.DENY, policy.decide(p(GuardianMethod.EXPLAIN_CLOUD, RiskLevel.MEDIUM), sms.copy(cloudAvailable = false)).dispatch)
    }

    @Test fun highRiskWithNoMethodGetsSafeDefault() {
        val d = policy.decide(p(GuardianMethod.NONE, RiskLevel.HIGH), sms)
        assertEquals(GuardianMethod.QUARANTINE_SMS, d.method); assertEquals(Dispatch.AUTO, d.dispatch)
    }

    @Test fun blockNeedsAReason() {
        val d = policy.decide(p(GuardianMethod.BLOCK_NUMBER, RiskLevel.CRITICAL, reasons = emptyList()), sms)
        assertEquals(GuardianMethod.QUARANTINE_SMS, d.method)
    }

    @Test fun rulesAreExemptFromConfidence() {
        val d = policy.decide(p(GuardianMethod.BLOCK_NUMBER, RiskLevel.CRITICAL, c = 10, src = VerdictSource.RULES), sms)
        assertEquals(GuardianMethod.BLOCK_NUMBER, d.method)
    }
}
