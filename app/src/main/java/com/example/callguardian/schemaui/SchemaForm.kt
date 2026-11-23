package com.example.callguardian.schemaui

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * The UI-facing view of a JSON Schema. The schema is the single source of truth for actions and
 * their UI: confirmation sheets and receipt views are built from this model at runtime, so adding a
 * method schema to the registry gives it working screens with no new UI code.
 *
 * Pure Kotlin (no Android types) so the mapping is unit-tested on the JVM (SchemaFormTest).
 */
enum class FieldKind { TEXT, MULTILINE, PHONE, EMAIL, INTEGER, NUMBER, BOOLEAN, ENUM, STRING_LIST, EPOCH_MILLIS, DATE_TIME, UNKNOWN }

data class SchemaField(
    val name: String,
    val title: String,
    val description: String?,
    val kind: FieldKind,
    val required: Boolean,
    val readOnly: Boolean,
    val enumValues: List<String> = emptyList(),
    val minimum: Double? = null,
    val maximum: Double? = null,
    val minLength: Int? = null,
    val maxLength: Int? = null,
    val maxItems: Int? = null,
    val default: JsonElement? = null,
)

data class SchemaForm(
    val id: String?,
    val title: String,
    val description: String?,
    val fields: List<SchemaField>,
) {
    /** Field-level problems for a draft value (mirrors the JSON Schema rules the renderer understands). */
    fun problems(values: JsonObject): Map<String, String> = buildMap {
        for (f in fields) {
            val v = values[f.name]
            val missing = v == null || v is JsonNull || (v is JsonPrimitive && v.isString && v.content.isBlank())
            if (missing) { if (f.required) put(f.name, "Required"); continue }
            when (f.kind) {
                FieldKind.INTEGER, FieldKind.EPOCH_MILLIS -> {
                    val n = (v as? JsonPrimitive)?.longOrNull
                    if (n == null) put(f.name, "Must be a whole number")
                    else if (f.minimum != null && n < f.minimum) put(f.name, "Must be ≥ ${fmtNum(f.minimum)}")
                    else if (f.maximum != null && n > f.maximum) put(f.name, "Must be ≤ ${fmtNum(f.maximum)}")
                }
                FieldKind.NUMBER -> {
                    val n = (v as? JsonPrimitive)?.doubleOrNull
                    if (n == null) put(f.name, "Must be a number")
                    else if (f.minimum != null && n < f.minimum) put(f.name, "Must be ≥ ${fmtNum(f.minimum)}")
                    else if (f.maximum != null && n > f.maximum) put(f.name, "Must be ≤ ${fmtNum(f.maximum)}")
                }
                FieldKind.ENUM -> if ((v as? JsonPrimitive)?.contentOrNull !in f.enumValues) put(f.name, "Pick one of ${f.enumValues.joinToString()}")
                FieldKind.BOOLEAN -> if ((v as? JsonPrimitive)?.booleanOrNull == null) put(f.name, "Must be yes or no")
                FieldKind.STRING_LIST -> {
                    val n = (v as? JsonArray)?.size
                    if (n == null) put(f.name, "Must be a list") else if (f.maxItems != null && n > f.maxItems) put(f.name, "At most ${f.maxItems} items")
                }
                else -> {
                    val s = (v as? JsonPrimitive)?.contentOrNull.orEmpty()
                    if (f.minLength != null && s.length < f.minLength) put(f.name, "At least ${f.minLength} characters")
                    else if (f.maxLength != null && s.length > f.maxLength) put(f.name, "At most ${f.maxLength} characters")
                    else if (f.kind == FieldKind.PHONE && !looksLikePhone(s)) put(f.name, "Not a phone number")
                    else if (f.kind == FieldKind.EMAIL && !s.contains('@')) put(f.name, "Not an email address")
                }
            }
        }
    }

    companion object {
        private val json = Json { ignoreUnknownKeys = true }

        fun parse(schemaText: String): SchemaForm = parse(json.parseToJsonElement(schemaText).jsonObject)

        fun parse(schema: JsonObject): SchemaForm {
            val props = schema["properties"]?.jsonObject ?: JsonObject(emptyMap())
            val required = schema["required"]?.jsonArray?.mapNotNull { it.jsonPrimitive.contentOrNull }?.toSet().orEmpty()
            val fields = props.map { (name, el) -> field(name, el.jsonObject, name in required) }
            return SchemaForm(schema.str("\$id"), schema.str("title") ?: "Untitled", schema.str("description"), fields)
        }

        fun field(name: String, p: JsonObject, required: Boolean): SchemaField {
            val type = typeOf(p)
            val format = p.str("format")
            val enumValues = p["enum"]?.jsonArray?.mapNotNull { it.jsonPrimitive.contentOrNull }.orEmpty()
            val maxLength = p.int("maxLength")
            val kind = when {
                enumValues.isNotEmpty() -> FieldKind.ENUM
                type == "boolean" -> FieldKind.BOOLEAN
                type == "integer" && format == "epoch-millis" -> FieldKind.EPOCH_MILLIS
                type == "integer" -> FieldKind.INTEGER
                type == "number" -> FieldKind.NUMBER
                type == "array" && p["items"]?.jsonObject?.let { typeOf(it) } == "string" -> FieldKind.STRING_LIST
                type == "string" && format == "phone" -> FieldKind.PHONE
                type == "string" && format == "email" -> FieldKind.EMAIL
                type == "string" && (format == "date-time" || format == "date") -> FieldKind.DATE_TIME
                type == "string" && (maxLength ?: 0) > 120 -> FieldKind.MULTILINE
                type == "string" -> FieldKind.TEXT
                else -> FieldKind.UNKNOWN
            }
            return SchemaField(
                name = name, title = p.str("title") ?: humanize(name), description = p.str("description"), kind = kind,
                required = required, readOnly = p["readOnly"]?.jsonPrimitive?.booleanOrNull == true, enumValues = enumValues,
                minimum = p.dbl("minimum"), maximum = p.dbl("maximum"), minLength = p.int("minLength"), maxLength = maxLength,
                maxItems = p.int("maxItems"), default = p["default"],
            )
        }

        /** "type" may be a string or ["string","null"]. */
        private fun typeOf(p: JsonObject): String? = when (val t = p["type"]) {
            is JsonPrimitive -> t.contentOrNull
            is JsonArray -> t.mapNotNull { it.jsonPrimitive.contentOrNull }.firstOrNull { it != "null" }
            else -> null
        }

        private fun JsonObject.str(k: String) = (this[k] as? JsonPrimitive)?.contentOrNull
        private fun JsonObject.int(k: String) = (this[k] as? JsonPrimitive)?.intOrNull
        private fun JsonObject.dbl(k: String) = (this[k] as? JsonPrimitive)?.doubleOrNull

        fun humanize(name: String): String = name
            .replace(Regex("([a-z0-9])([A-Z])"), "$1 $2").replace('_', ' ').replace('-', ' ')
            .lowercase().replaceFirstChar { it.uppercase() }

        /** "PHONE_NUMBER" / "SILENCE" -> "Phone number" / "Silence". */
        fun humanizeEnum(v: String): String = v.replace('_', ' ').lowercase().replaceFirstChar { it.uppercase() }

        fun looksLikePhone(s: String): Boolean = s == "hidden" || s.contains('@') || s.count { it.isDigit() } in 3..17

        private fun fmtNum(d: Double) = if (d % 1.0 == 0.0) d.toLong().toString() else d.toString()

        /** Human display of a value for a field kind (used by receipt detail and read-only rows). */
        fun display(f: SchemaField, v: JsonElement?): String = when {
            v == null || v is JsonNull -> "—"
            v is JsonArray -> v.joinToString("\n") { "• " + ((it as? JsonPrimitive)?.contentOrNull ?: it.toString()) }
            v is JsonObject -> v.toString()
            else -> {
                val p = v as JsonPrimitive
                when (f.kind) {
                    FieldKind.BOOLEAN -> if (p.booleanOrNull == true) "Yes" else "No"
                    FieldKind.ENUM -> humanizeEnum(p.content)
                    FieldKind.EPOCH_MILLIS -> p.longOrNull?.let { SimpleDateFormat("MMM d, yyyy HH:mm:ss", Locale.US).format(Date(it)) } ?: p.content
                    FieldKind.PHONE -> formatPhone(p.content)
                    else -> p.content
                }
            }
        }

        fun formatPhone(s: String): String {
            val d = s.filter { it.isDigit() }
            return when {
                d.length == 11 && d.startsWith("1") -> "+1 (${d.substring(1, 4)}) ${d.substring(4, 7)}-${d.substring(7)}"
                d.length == 10 && !s.startsWith("+") -> "(${d.substring(0, 3)}) ${d.substring(3, 6)}-${d.substring(6)}"
                else -> s
            }
        }

        /** One-line summary of an object for list rows, ordered by the schema (required fields first). */
        fun summary(form: SchemaForm, value: JsonObject, max: Int = 3): String =
            form.fields.sortedByDescending { it.required }
                .filter { value[it.name] != null && value[it.name] !is JsonNull && it.kind != FieldKind.MULTILINE && it.kind != FieldKind.STRING_LIST }
                .take(max).joinToString(" · ") { display(it, value[it.name]) }
    }
}
