package com.example.callguardian.policy

import com.fasterxml.jackson.databind.ObjectMapper
import com.networknt.schema.JsonSchema
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SchemaValidatorsConfig
import com.networknt.schema.SpecVersion

/** Result of checking one raw model response against a JSON Schema (draft-07). */
data class SchemaReport(
    val schemaId: String,
    val valid: Boolean,
    val errors: List<String>,
    /** The JSON text that was actually validated (after removing code fences, if any). */
    val json: String,
)

/**
 * Validates raw model output against a published JSON Schema *before* anything acts on it.
 * The model never executes an action; it only proposes one, and a proposal that does not match
 * the schema exactly (unknown action, out-of-range confidence, extra fields, ...) is discarded.
 */
class SchemaValidator(schemaJson: String, val schemaId: String) {

    private val mapper = ObjectMapper()
    // `format` is a UI hint here (phone, epoch-millis drive the schema-rendered forms); the contract is
    // enforced by type/pattern/enum/bounds. Format assertions are off so that, e.g., the validator's
    // built-in E.164 "phone" check doesn't reject national numbers or email-to-SMS senders that the
    // schema's own `pattern` allows.
    private val schema: JsonSchema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7)
        .getSchema(schemaJson, SchemaValidatorsConfig.builder().formatAssertionsEnabled(false).build())

    fun validate(rawModelText: String?): SchemaReport {
        val json = extractJson(rawModelText.orEmpty())
        if (json.isBlank()) {
            return SchemaReport(schemaId, false, listOf("Model returned no JSON"), json)
        }
        val node = try {
            mapper.readTree(json)
        } catch (e: Exception) {
            return SchemaReport(schemaId, false, listOf("Not valid JSON: ${e.message?.lineSequence()?.firstOrNull()}"), json)
        }
        val messages = schema.validate(node).map { it.message }.sorted()
        return SchemaReport(schemaId, messages.isEmpty(), messages, json)
    }

    companion object {
        /** Small models sometimes wrap JSON in ```json fences; strip those, nothing else. */
        fun extractJson(text: String): String {
            var t = text.trim()
            if (t.startsWith("```")) {
                t = t.removePrefix("```json").removePrefix("```JSON").removePrefix("```")
                t = t.substringBeforeLast("```")
            }
            return t.trim()
        }
    }
}
