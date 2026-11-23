package com.example.callguardian

import com.example.callguardian.policy.SchemaValidator
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SchemaValidatorTest {
    private val v = SchemaValidator(TestAssets.read("scam_verdict.schema.json"), "scam_verdict.schema.json")
    private val good = """{"riskLevel":"HIGH","category":"PACKAGE_DELIVERY","confidence":91,"reasons":["Look-alike USPS link"],"method":"QUARANTINE_SMS","advice":"Don't tap the link."}"""

    @Test fun acceptsValidVerdict() = assertTrue(v.validate(good).errors.toString(), v.validate(good).valid)
    @Test fun stripsCodeFences() = assertTrue(v.validate("```json\n$good\n```").valid)
    @Test fun rejectsUnknownMethod() = assertFalse(v.validate(good.replace("QUARANTINE_SMS", "DELETE_ALL_SMS")).valid)
    @Test fun rejectsOutOfRangeConfidence() = assertFalse(v.validate(good.replace("91", "140")).valid)
    @Test fun rejectsExtraFields() = assertFalse(v.validate(good.replace("}", ",\"tool\":\"send_sms\"}")).valid)
    @Test fun rejectsTooManyReasons() = assertFalse(v.validate(good.replace("[\"Look-alike USPS link\"]", "[\"a\",\"b\",\"c\",\"d\"]")).valid)
    @Test fun rejectsGarbage() { assertFalse(v.validate("I think this is a scam").valid); assertFalse(v.validate(null).valid) }
}
