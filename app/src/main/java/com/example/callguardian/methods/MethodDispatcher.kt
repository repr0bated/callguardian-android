package com.example.callguardian.methods

import android.content.Intent
import com.example.callguardian.data.db.ReceiptDao
import com.example.callguardian.data.db.ReceiptEntity
import com.example.callguardian.model.GuardianMethod
import com.example.callguardian.model.VerdictSource
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

data class DispatchResult(
    val receiptId: Long,
    val status: String,
    val output: JsonElement?,
    /** Some methods (report_scam, save_contact) hand an Intent back for the UI to launch. */
    val launch: Intent? = null,
    val error: String? = null,
) {
    val ok get() = status == STATUS_OK

    companion object {
        const val STATUS_OK = "OK"
        const val STATUS_DENIED = "DENIED"
        const val STATUS_INVALID_INPUT = "INVALID_INPUT"
        const val STATUS_INVALID_OUTPUT = "INVALID_OUTPUT"
        const val STATUS_FAILED = "FAILED"
    }
}

/**
 * The only path from a proposal to a side effect:
 *   registry lookup → input JSON Schema validation → decode into the generated Kotlin type
 *   → handler → output JSON Schema validation → receipt in Room.
 * Denied proposals get a receipt too, so the audit trail shows what the model *wanted*.
 */
@Singleton
class MethodDispatcher @Inject constructor(
    private val registry: MethodRegistry,
    private val handlers: MethodHandlers,
    private val receipts: ReceiptDao,
) {
    suspend fun dispatch(
        method: GuardianMethod,
        input: JsonObject,
        eventId: Long?,
        proposedBy: VerdictSource,
        dispatch: String, // AUTO | USER_CONFIRMED
        notes: List<String> = emptyList(),
        extras: HandlerExtras = HandlerExtras(),
    ): DispatchResult {
        val reg = registry.byMethod(method) ?: return DispatchResult(-1, DispatchResult.STATUS_DENIED, null, error = "Unknown method $method")
        val started = System.currentTimeMillis()
        val inputText = input.toString()

        suspend fun receipt(status: String, out: String?, extraNotes: List<String>) = receipts.insert(ReceiptEntity(
            eventId = eventId, methodId = reg.id, registryVersion = registry.version,
            inputSchema = reg.inputSchemaId, outputSchema = reg.outputSchemaId,
            inputJson = inputText, outputJson = out, executedOn = reg.spec.execution.name,
            proposedBy = proposedBy.name, dispatch = dispatch, status = status,
            notes = (notes + extraNotes).joinToString("\n"), createdAt = started,
            durationMs = System.currentTimeMillis() - started,
        ))

        val inReport = reg.inputValidator.validate(inputText)
        if (!inReport.valid) {
            val id = receipt(DispatchResult.STATUS_INVALID_INPUT, null, inReport.errors.map { "input: $it" })
            return DispatchResult(id, DispatchResult.STATUS_INVALID_INPUT, null, error = inReport.errors.joinToString("; "))
        }
        val outcome = try {
            handlers.execute(method, input, extras)
        } catch (t: Throwable) {
            Timber.w(t, "Method %s failed", reg.id)
            val id = receipt(DispatchResult.STATUS_FAILED, null, listOf("error: ${t.message ?: t.javaClass.simpleName}"))
            return DispatchResult(id, DispatchResult.STATUS_FAILED, null, error = t.message)
        }
        val outText = outcome.output.toString()
        val outReport = reg.outputValidator.validate(outText)
        if (!outReport.valid) {
            val id = receipt(DispatchResult.STATUS_INVALID_OUTPUT, outText, outReport.errors.map { "output: $it" })
            return DispatchResult(id, DispatchResult.STATUS_INVALID_OUTPUT, outcome.output, error = outReport.errors.joinToString("; "))
        }
        val id = receipt(DispatchResult.STATUS_OK, outText, outcome.notes)
        return DispatchResult(id, DispatchResult.STATUS_OK, outcome.output, outcome.launch)
    }

    /** Records a policy denial without running anything. */
    suspend fun recordDenied(method: GuardianMethod, input: JsonObject, eventId: Long?, proposedBy: VerdictSource, notes: List<String>) {
        val reg = registry.byMethod(method) ?: return
        receipts.insert(ReceiptEntity(
            eventId = eventId, methodId = reg.id, registryVersion = registry.version,
            inputSchema = reg.inputSchemaId, outputSchema = reg.outputSchemaId, inputJson = input.toString(),
            executedOn = reg.spec.execution.name, proposedBy = proposedBy.name, dispatch = "DENY",
            status = DispatchResult.STATUS_DENIED, notes = notes.joinToString("\n"),
        ))
    }
}
