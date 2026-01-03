package com.callguardian.app.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.callguardian.app.model.BlockMode
import java.time.Instant

@Entity(tableName = "phone_profiles")
data class PhoneProfileEntity(
    @PrimaryKey
    @ColumnInfo(name = "phone_number")
    val phoneNumber: String,
    @ColumnInfo(name = "display_name")
    val displayName: String? = null,
    @ColumnInfo(name = "carrier")
    val carrier: String? = null,
    @ColumnInfo(name = "region")
    val region: String? = null,
    @ColumnInfo(name = "line_type")
    val lineType: String? = null,
    @ColumnInfo(name = "last_lookup_at")
    val lastLookupAt: Instant = Instant.now(),
    @ColumnInfo(name = "spam_score")
    val spamScore: Int? = null,
    @ColumnInfo(name = "tags")
    val tags: List<String> = emptyList(),
    @ColumnInfo(name = "block_mode")
    val blockMode: BlockMode = BlockMode.NONE,
    @ColumnInfo(name = "notes")
    val notes: String? = null,
    @ColumnInfo(name = "contact_id")
    val contactId: Long? = null,
    @ColumnInfo(name = "is_existing_contact")
    val isExistingContact: Boolean = false
) {
    fun shouldBlockCalls(): Boolean = blockMode == BlockMode.CALLS || blockMode == BlockMode.ALL
    fun shouldBlockMessages(): Boolean = blockMode == BlockMode.MESSAGES || blockMode == BlockMode.ALL
}
