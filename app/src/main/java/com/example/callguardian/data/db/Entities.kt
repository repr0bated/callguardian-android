package com.example.callguardian.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.callguardian.model.BlockMode

/** One screened call or SMS, plus everything each tier concluded about it. */
@Entity(tableName = "screening_events", indices = [Index("number"), Index("received_at")])
data class ScreeningEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val channel: String,
    val number: String?,
    @ColumnInfo(name = "display_name") val displayName: String? = null,
    val body: String? = null,
    @ColumnInfo(name = "received_at") val receivedAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "is_contact") val isContact: Boolean = false,
    @ColumnInfo(name = "is_test") val isTest: Boolean = false,
    @ColumnInfo(name = "lookup_summary") val lookupSummary: String? = null,

    // Tier 1: rules
    @ColumnInfo(name = "rules_score") val rulesScore: Int = 0,
    @ColumnInfo(name = "rules_risk") val rulesRisk: String = "SAFE",
    @ColumnInfo(name = "rules_flags") val rulesFlags: String = "",
    @ColumnInfo(name = "rules_micros") val rulesMicros: Long = 0,

    // Tier 2: on-device model (Gemma 4 / Gemini Nano via Firebase AI Logic)
    @ColumnInfo(name = "ai_status") val aiStatus: String = AiStatus.NOT_RUN,
    @ColumnInfo(name = "ai_source") val aiSource: String? = null,
    @ColumnInfo(name = "ai_model") val aiModel: String? = null,
    @ColumnInfo(name = "ai_raw") val aiRaw: String? = null,
    @ColumnInfo(name = "ai_latency_ms") val aiLatencyMs: Long? = null,
    @ColumnInfo(name = "ai_error") val aiError: String? = null,
    @ColumnInfo(name = "schema_valid") val schemaValid: Boolean? = null,
    @ColumnInfo(name = "schema_errors") val schemaErrors: String? = null,

    // Final decision
    @ColumnInfo(name = "risk") val risk: String = "SAFE",
    val category: String = "NONE",
    val confidence: Int = 0,
    val reasons: String = "",
    val advice: String? = null,
    @ColumnInfo(name = "decided_by") val decidedBy: String = "RULES",
    /** Registry method alias proposed by the deciding tier (e.g. block_number) or "none". */
    @ColumnInfo(name = "proposed_action") val proposedAction: String = "none",
    /** Method alias the capability policy settled on (may be a safer fallback). */
    @ColumnInfo(name = "final_action") val finalAction: String = "none",
    @ColumnInfo(name = "auto_applied") val autoApplied: Boolean = false,
    @ColumnInfo(name = "gate_notes") val gateNotes: String = "",

    // Tier 3: cloud deep analysis (opt-in, redacted)
    @ColumnInfo(name = "deep_status") val deepStatus: String = AiStatus.NOT_RUN,
    @ColumnInfo(name = "deep_raw") val deepRaw: String? = null,
    @ColumnInfo(name = "deep_redacted_input") val deepRedactedInput: String? = null,
    @ColumnInfo(name = "deep_latency_ms") val deepLatencyMs: Long? = null,
    @ColumnInfo(name = "deep_error") val deepError: String? = null,
    @ColumnInfo(name = "deep_schema_valid") val deepSchemaValid: Boolean? = null,

    @ColumnInfo(name = "user_action") val userAction: String? = null,
    /** AUTO | OFFER | DENY from the capability policy for [finalAction]. */
    @ColumnInfo(name = "policy_dispatch") val policyDispatch: String = "DENY",
    val quarantined: Boolean = false,
)

object AiStatus {
    const val NOT_RUN = "NOT_RUN"
    const val PENDING = "PENDING"
    const val RUNNING = "RUNNING"
    const val DONE = "DONE"
    const val FAILED = "FAILED"
    const val REJECTED = "REJECTED" // ran, but output failed schema validation
}

@Entity(tableName = "phone_profiles")
data class PhoneProfileEntity(
    @PrimaryKey @ColumnInfo(name = "phone_number") val phoneNumber: String,
    @ColumnInfo(name = "display_name") val displayName: String? = null,
    val carrier: String? = null,
    val region: String? = null,
    @ColumnInfo(name = "line_type") val lineType: String? = null,
    @ColumnInfo(name = "spam_score") val spamScore: Int? = null,
    val tags: String = "",
    @ColumnInfo(name = "block_mode") val blockMode: String = BlockMode.NONE.name,
    val trusted: Boolean = false,
    @ColumnInfo(name = "updated_at") val updatedAt: Long = System.currentTimeMillis(),
) {
    val mode: BlockMode get() = runCatching { BlockMode.valueOf(blockMode) }.getOrDefault(BlockMode.NONE)
    fun shouldBlockCalls() = mode == BlockMode.CALLS || mode == BlockMode.ALL
    fun shouldBlockMessages() = mode == BlockMode.MESSAGES || mode == BlockMode.ALL
}

/** Audit record for every typed-method dispatch attempt (executed, denied, or invalid). */
@Entity(tableName = "method_receipts", indices = [Index("event_id"), Index("created_at")])
data class ReceiptEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "event_id") val eventId: Long?,
    @ColumnInfo(name = "method_id") val methodId: String,
    @ColumnInfo(name = "registry_version") val registryVersion: String,
    @ColumnInfo(name = "input_schema") val inputSchema: String,
    @ColumnInfo(name = "output_schema") val outputSchema: String,
    @ColumnInfo(name = "input_json") val inputJson: String,
    @ColumnInfo(name = "output_json") val outputJson: String? = null,
    /** device | cloud — where the method body executed (from the registry). */
    @ColumnInfo(name = "executed_on") val executedOn: String,
    /** RULES | ON_DEVICE | IN_CLOUD | USER — who proposed it. */
    @ColumnInfo(name = "proposed_by") val proposedBy: String,
    /** AUTO | USER_CONFIRMED | DENY */
    val dispatch: String,
    /** OK | DENIED | INVALID_INPUT | INVALID_OUTPUT | FAILED */
    val status: String,
    val notes: String = "",
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "duration_ms") val durationMs: Long = 0,
)
