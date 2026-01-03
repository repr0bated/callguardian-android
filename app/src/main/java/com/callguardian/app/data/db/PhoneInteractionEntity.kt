package com.callguardian.app.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.callguardian.app.model.InteractionDirection
import com.callguardian.app.model.InteractionType
import java.time.Instant

@Entity(tableName = "phone_interactions")
data class PhoneInteractionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "phone_number")
    val phoneNumber: String,
    @ColumnInfo(name = "type")
    val type: InteractionType,
    @ColumnInfo(name = "direction")
    val direction: InteractionDirection,
    @ColumnInfo(name = "timestamp")
    val timestamp: Instant = Instant.now(),
    @ColumnInfo(name = "status")
    val status: String,
    @ColumnInfo(name = "message_body")
    val messageBody: String? = null,
    @ColumnInfo(name = "lookup_summary")
    val lookupSummary: String? = null
)
