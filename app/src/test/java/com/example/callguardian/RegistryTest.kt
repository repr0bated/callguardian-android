package com.example.callguardian

import com.example.callguardian.methods.BindingContext
import com.example.callguardian.methods.MethodBinder
import com.example.callguardian.model.Channel
import com.example.callguardian.model.GuardianMethod
import com.example.callguardian.model.RiskLevel
import com.example.callguardian.model.ScamCategory
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RegistryTest {
    private val reg = TestAssets.registry

    private fun enumOf(schemaFile: String, prop: String): Set<String> =
        Json.parseToJsonElement(TestAssets.read(schemaFile)).jsonObject["properties"]!!.jsonObject[prop]!!.jsonObject["enum"]!!
            .jsonArray.map { it.jsonPrimitive.content }.toSet()

    @Test fun registryLoadsAndIsVersioned() {
        assertEquals(7, reg.methods.size)
        assertTrue(Regex("^[0-9]{4}\\.[0-9]{2}\\.[0-9]+$").matches(reg.version))
        reg.methods.forEach { assertTrue(it.id, Regex("^guardian\\.[a-z]+\\.[a-z]+@v[0-9]+$").matches(it.id)) }
        assertNotNull(reg.byId("guardian.call.block@v1"))
    }

    /** The Kotlin enum (needed by @Generable), the schema enums and the registry must agree. */
    @Test fun guardianMethodEnumMatchesSchemasAndRegistry() {
        val names = GuardianMethod.entries.map { it.name }.toSet()
        assertEquals(names, enumOf("scam_verdict.schema.json", "method"))
        assertEquals(names - "EXPLAIN_CLOUD", enumOf("deep_analysis.schema.json", "method"))
        assertEquals(GuardianMethod.entries.filter { it != GuardianMethod.NONE }.map { it.alias }.toSet(), reg.methods.map { it.spec.alias }.toSet())
        GuardianMethod.entries.filter { it != GuardianMethod.NONE }.forEach { assertNotNull(it.name, reg.byMethod(it)) }
    }

    @Test fun categoryAndRiskEnumsMatchSchema() {
        assertEquals(ScamCategory.entries.map { it.name }.toSet(), enumOf("scam_verdict.schema.json", "category"))
        assertEquals(RiskLevel.entries.map { it.name }.toSet(), enumOf("scam_verdict.schema.json", "riskLevel"))
    }

    /** The binder must only ever produce inputs that pass the method's own input schema. */
    @Test fun boundInputsValidateForEveryMethodAndChannel() {
        for (ch in Channel.entries) for (num in listOf("+15550100199", null)) {
            val ctx = BindingContext(42, ch, num, "Ann", RiskLevel.HIGH, ScamCategory.PACKAGE_DELIVERY, listOf("Link to look-alike USPS domain"),
                redactedText = "Sender: ••99\nUSPS: package on hold https://usps-help.top/[path]", onDeviceOpinion = "HIGH, PACKAGE_DELIVERY, 90")
            reg.methods.filter { m -> m.spec.channels.any { it.name == ch.name } }.forEach { m ->
                val needsNumber = m.method in listOf(GuardianMethod.BLOCK_NUMBER, GuardianMethod.ALLOW_NUMBER, GuardianMethod.SAVE_CONTACT)
                if (num == null && needsNumber) return@forEach
                val r = m.inputValidator.validate(MethodBinder.bind(m.method, ctx).toString())
                assertTrue("${m.id} $ch: ${r.errors}", r.valid)
            }
        }
    }

    @Test fun inputSchemasRejectUnknownAndMissingFields() {
        val block = reg.byMethod(GuardianMethod.BLOCK_NUMBER)!!
        assertTrue(!block.inputValidator.validate("""{"number":"+15550100199","scope":"ALL"}""").valid)
        assertTrue(!block.inputValidator.validate("""{"number":"+15550100199","scope":"ALL","reason":"x","extra":1}""").valid)
        assertTrue(!block.inputValidator.validate("""{"number":"click here","scope":"ALL","reason":"x"}""").valid)
    }
}
