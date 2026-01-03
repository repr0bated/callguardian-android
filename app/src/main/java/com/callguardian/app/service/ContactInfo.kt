package com.callguardian.app.service

/**
 * Data class representing contact information for a phone number.
 */
data class ContactInfo(
    val phoneNumber: String,
    val contactId: Long?,
    val existsInContacts: Boolean,
    val displayName: String? = null,
    val profilePhoto: String? = null,
    val isSpam: Boolean = false,
    val isBlocked: Boolean = false,
    val tags: List<String> = emptyList(),
    val lastModified: Long = System.currentTimeMillis()
)

/**
 * Data class representing a field of contact information.
 */
data class ContactInfoField(
    val type: FieldType,
    val value: String,
    val label: String? = null,
    val isPrimary: Boolean = false
)

/**
 * Enum representing different types of contact information fields.
 */
enum class FieldType {
    NAME,
    PHONE,
    EMAIL,
    ADDRESS,
    ORGANIZATION,
    JOB_TITLE,
    WEBSITE,
    NOTE,
    PHOTO,
    CUSTOM
}

/**
 * Data class representing a change detected in contact synchronization.
 */
data class ContactChange(
    val type: ChangeType,
    val field: ContactInfoField,
    val oldValue: String? = null,
    val newValue: String? = null
)

/**
 * Enum representing different types of contact changes.
 */
enum class ChangeType {
    ADDED,
    MODIFIED,
    REMOVED,
    UPDATED
}

/**
 * Sealed class representing the result of contact synchronization analysis.
 */
sealed class ContactSyncResult {
    object NoChanges : ContactSyncResult()
    
    data class ChangesDetected(
        val contactInfo: ContactInfo,
        val changes: List<ContactChange>,
        val syncTimestamp: Long = System.currentTimeMillis()
    ) : ContactSyncResult()
}