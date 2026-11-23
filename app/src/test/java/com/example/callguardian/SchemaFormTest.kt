package com.example.callguardian

import com.example.callguardian.schemaui.FieldKind
import com.example.callguardian.schemaui.SchemaForm
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SchemaFormTest {

    private fun inputOf(alias: String) = TestAssets.registry.methods.first { it.spec.alias == alias }.let { SchemaForm.parse(it.inputSchemaJson) }
    private fun outputOf(alias: String) = TestAssets.registry.methods.first { it.spec.alias == alias }.let { SchemaForm.parse(it.outputSchemaJson) }

    @Test fun blockInputMapsToPhoneEnumAndText() {
        val f = inputOf("block_number")
        assertEquals("BlockNumberInput", f.title)
        val number = f.fields.first { it.name == "number" }
        assertEquals(FieldKind.PHONE, number.kind)
        assertTrue(number.required); assertTrue(number.readOnly)
        assertEquals("Sender", number.title)
        val scope = f.fields.first { it.name == "scope" }
        assertEquals(FieldKind.ENUM, scope.kind)
        assertEquals(listOf("CALLS", "MESSAGES", "ALL"), scope.enumValues)
        val reason = f.fields.first { it.name == "reason" }
        assertEquals(FieldKind.MULTILINE, reason.kind) // maxLength 200 > 120
        assertEquals(200, reason.maxLength)
    }

    @Test fun blockOutputHasEpochMillisAndBoolean() {
        val f = outputOf("block_number")
        assertEquals(FieldKind.EPOCH_MILLIS, f.fields.first { it.name == "blockedAt" }.kind)
        assertEquals(FieldKind.BOOLEAN, f.fields.first { it.name == "previouslyBlocked" }.kind)
    }

    @Test fun explainOutputHasStringListsAndBoundedInteger() {
        val f = outputOf("explain_cloud")
        assertEquals(FieldKind.STRING_LIST, f.fields.first { it.name == "tactics" }.kind)
        val conf = f.fields.first { it.name == "confidence" }
        assertEquals(FieldKind.INTEGER, conf.kind)
        assertEquals(0.0, conf.minimum); assertEquals(100.0, conf.maximum)
    }

    @Test fun optionalFieldsAreNotRequired() {
        val f = inputOf("save_contact")
        assertFalse(f.fields.first { it.name == "displayName" }.required)
    }

    /** "Add a schema, get its screens": every shipped method schema must map with no UNKNOWN fields and a title per field. */
    @Test fun everyRegistrySchemaRendersWithoutUnknownFields() {
        TestAssets.registry.methods.forEach { m ->
            listOf(m.inputSchemaJson, m.outputSchemaJson).forEach { s ->
                val form = SchemaForm.parse(s)
                assertTrue("${m.id} has no fields", form.fields.isNotEmpty())
                form.fields.forEach { assertTrue("${m.id}.${it.name} is UNKNOWN", it.kind != FieldKind.UNKNOWN) }
            }
        }
    }

    @Test fun problemsFollowSchemaRules() {
        val f = inputOf("block_number")
        val ok = buildJsonObject { put("number", "+15550100199"); put("scope", "ALL"); put("reason", "Toll scam") }
        assertTrue(f.problems(ok).isEmpty())
        val missing = buildJsonObject { put("number", "+15550100199"); put("scope", "NOPE") }
        val p = f.problems(missing)
        assertEquals("Required", p["reason"])
        assertTrue(p["scope"]!!.startsWith("Pick one of"))
        val tooLong = JsonObject(ok + ("reason" to JsonPrimitive("x".repeat(201))))
        assertEquals("At most 200 characters", f.problems(tooLong)["reason"])
    }

    @Test fun integerBoundsAndListsValidate() {
        val form = SchemaForm.parse("""{"title":"T","type":"object","required":["n","l"],"properties":{
            "n":{"type":"integer","minimum":0,"maximum":10},"l":{"type":"array","items":{"type":"string"},"maxItems":2}}}""")
        val bad = JsonObject(mapOf("n" to JsonPrimitive(11), "l" to JsonArray(List(3) { JsonPrimitive("a") })))
        val p = form.problems(bad)
        assertEquals("Must be ≤ 10", p["n"]); assertEquals("At most 2 items", p["l"])
    }

    @Test fun nullableTypeArraysAndUntypedEnums() {
        val form = SchemaForm.parse("""{"properties":{"a":{"type":["string","null"],"format":"email"},"b":{"enum":["X_Y","Z"]}}}""")
        assertEquals(FieldKind.EMAIL, form.fields[0].kind)
        assertEquals(FieldKind.ENUM, form.fields[1].kind)
        assertEquals("Untitled", form.title)
        assertEquals("X y", SchemaForm.humanizeEnum("X_Y"))
    }

    @Test fun humanizeAndDisplay() {
        assertEquals("Previously blocked", SchemaForm.humanize("previouslyBlocked"))
        assertEquals("Event id", SchemaForm.humanize("event_id"))
        val f = outputOf("block_number")
        assertEquals("Yes", SchemaForm.display(f.fields.first { it.name == "previouslyBlocked" }, JsonPrimitive(true)))
        assertEquals("+1 (555) 010-0199", SchemaForm.formatPhone("+15550100199"))
        val summary = SchemaForm.summary(f, buildJsonObject { put("number", "+15550100199"); put("scope", "ALL"); put("previouslyBlocked", false); put("blockedAt", 0L) })
        assertTrue(summary, summary.startsWith("+1 (555) 010-0199"))
    }
}
