package com.example.callguardian.methods

import com.example.callguardian.methods.gen.Registry
import com.example.callguardian.model.GuardianMethod
import com.example.callguardian.policy.SchemaValidator
import kotlinx.serialization.json.Json

/** A registry entry plus its compiled input/output validators and raw schemas (for the UI renderer). */
class RegisteredMethod(
    val spec: Registry.MethodSpec,
    val inputSchemaJson: String,
    val outputSchemaJson: String,
) {
    val id: String get() = spec.id
    val method: GuardianMethod get() = GuardianMethod.fromAlias(spec.alias)
    val inputValidator = SchemaValidator(inputSchemaJson, spec.input.substringAfterLast('/'))
    val outputValidator = SchemaValidator(outputSchemaJson, spec.output.substringAfterLast('/'))
    val inputSchemaId: String get() = spec.input.substringAfterLast('/')
    val outputSchemaId: String get() = spec.output.substringAfterLast('/')
}

/**
 * Versioned registry of typed methods, loaded from assets/schemas/registry.json and checked
 * against registry.schema.json. The Kotlin type [Registry] is generated from that schema at build
 * time (json-kotlin-schema-codegen), so the file, the validator and the code can't disagree.
 *
 * @param read loads a file relative to assets/schemas (Android assets in the app, files in tests).
 */
class MethodRegistry(read: (String) -> String) {

    val registry: Registry
    val methods: List<RegisteredMethod>

    init {
        val text = read("registry.json")
        val report = SchemaValidator(read("registry.schema.json"), "registry.schema.json").validate(text)
        require(report.valid) { "registry.json is invalid: ${report.errors}" }
        registry = json.decodeFromString(Registry.serializer(), text)
        methods = registry.methods.map { RegisteredMethod(it, read(it.input), read(it.output)) }
        require(methods.map { it.id }.toSet().size == methods.size) { "Duplicate method id in registry" }
    }

    val version: String get() = registry.registryVersion

    fun byMethod(m: GuardianMethod): RegisteredMethod? = methods.firstOrNull { it.method == m }
    fun byId(id: String): RegisteredMethod? = methods.firstOrNull { it.id == id }

    companion object {
        val json = Json { ignoreUnknownKeys = false; encodeDefaults = true; explicitNulls = false }
    }
}
