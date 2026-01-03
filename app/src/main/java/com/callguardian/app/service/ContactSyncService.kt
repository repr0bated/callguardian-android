package com.callguardian.app.service

import android.content.ContentProviderOperation
import android.content.Context
import android.provider.ContactsContract
import com.callguardian.app.model.LookupOutcome
import com.callguardian.app.model.LookupResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service for synchronizing local contacts with reverse lookup data
 * Compares existing contact information with lookup results and identifies changes
 */
@Singleton
class ContactSyncService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val contactLookupService: ContactLookupService
) {

    /**
     * Analyzes a phone number and returns sync recommendations
     */
    suspend fun analyzeContactSync(
        phoneNumber: String,
        lookupOutcome: LookupOutcome
    ): ContactSyncResult = withContext(Dispatchers.IO) {
        val contactInfo = lookupOutcome.contactInfo
        val lookupResult = lookupOutcome.lookupResult

        if (contactInfo == null || lookupResult == null) {
            return@withContext ContactSyncResult.NoChanges
        }

        // Get existing contact details
        val existingContact = getExistingContactDetails(contactInfo.contactId)

        // Compare and identify changes
        val changes = mutableListOf<ContactChange>()

        // Check display name changes
        if (existingContact.displayName != lookupResult.displayName &&
            !lookupResult.displayName.isNullOrBlank()) {
            changes.add(
                ContactChange(
                    type = ChangeType.MODIFIED,
                    field = ContactInfoField(FieldType.NAME, lookupResult.displayName!!),
                    oldValue = existingContact.displayName ?: phoneNumber,
                    newValue = lookupResult.displayName!!
                )
            )
        }

        return@withContext if (changes.isNotEmpty()) {
            ContactSyncResult.ChangesDetected(contactInfo, changes)
        } else {
            ContactSyncResult.NoChanges
        }
    }

    /**
     * Applies approved changes to a contact
     */
    suspend fun applyContactChanges(
        contactInfo: ContactInfo,
        approvedChanges: List<ContactChange>
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val operations = mutableListOf<ContentProviderOperation>()

            // Build update operations for approved changes
            approvedChanges.forEach { change ->
                when (change.type) {
                    ChangeType.MODIFIED -> {
                        if (change.field.type == FieldType.NAME) {
                            operations.add(
                                ContentProviderOperation.newUpdate(
                                    ContactsContract.Data.CONTENT_URI
                                )
                                    .withSelection(
                                        "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
                                        arrayOf(contactInfo.contactId.toString(), ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                                    )
                                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, change.newValue)
                                    .build()
                            )
                        }
                    }
                    else -> {}
                }
            }

            if (operations.isNotEmpty()) {
                val results = context.contentResolver.applyBatch(ContactsContract.AUTHORITY, operations)
                Timber.d("Applied ${operations.size} contact changes for ${contactInfo.displayName}")
                return@withContext results.all { it.count ?: 0 > 0 }
            }

            return@withContext true
        } catch (e: Exception) {
            Timber.e(e, "Failed to apply contact changes")
            return@withContext false
        }
    }

    private suspend fun getExistingContactDetails(contactId: Long?): ExistingContactDetails {
        if (contactId == null) return ExistingContactDetails()
        return try {
            val projection = arrayOf(
                ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Organization.COMPANY,
                ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY
            )

            context.contentResolver.query(
                ContactsContract.Data.CONTENT_URI,
                projection,
                "${ContactsContract.Data.CONTACT_ID} = ?",
                arrayOf(contactId.toString()),
                null
            )?.use { cursor ->
                val displayName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME))
                val phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val carrier = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Organization.COMPANY))
                val region = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY))

                // Get existing tags/notes
                val tags = getExistingTags(contactId)

                ExistingContactDetails(
                    displayName = displayName,
                    phoneNumber = phoneNumber,
                    carrier = carrier,
                    region = region,
                    tags = tags
                )
            } ?: ExistingContactDetails()
        } catch (e: Exception) {
            Timber.w(e, "Failed to get existing contact details")
            ExistingContactDetails()
        }
    }

    private suspend fun getExistingTags(contactId: Long): List<String> {
        return try {
            context.contentResolver.query(
                ContactsContract.Data.CONTENT_URI,
                arrayOf(ContactsContract.CommonDataKinds.Note.NOTE),
                "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
                arrayOf(contactId.toString(), ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE),
                null
            )?.use { cursor ->
                val tags = mutableListOf<String>()
                while (cursor.moveToNext()) {
                    val note = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Note.NOTE))
                    if (note.startsWith("CallGuardian Tags:")) {
                        tags.addAll(note.substringAfter("CallGuardian Tags:").split("\n").map { it.trim() })
                    }
                }
                tags
            } ?: emptyList()
        } catch (e: Exception) {
            Timber.w(e, "Failed to get existing tags")
            emptyList()
        }
    }

    private fun calculateNameConfidence(lookupResult: LookupResult, existing: ExistingContactDetails): Double {
        // Higher confidence if lookup source is reliable and name matches expected patterns
        var confidence = 0.5 // Base confidence

        // Boost confidence for known reliable sources
        if (lookupResult.source.contains("NumLookup") || lookupResult.source.contains("Abstract")) {
            confidence += 0.3
        }

        // Boost confidence if name looks like a real name (contains spaces, proper casing)
        if (lookupResult.displayName?.contains(" ") == true &&
            lookupResult.displayName.any { it.isUpperCase() }) {
            confidence += 0.2
        }

        // Reduce confidence if name is very different from phone number
        val nameLength = lookupResult.displayName?.length ?: 0
        if (nameLength < 3 || nameLength > 50) {
            confidence -= 0.2
        }

        return confidence.coerceIn(0.0, 1.0)
    }
}

data class ExistingContactDetails(
    val displayName: String? = null,
    val phoneNumber: String? = null,
    val carrier: String? = null,
    val region: String? = null,
    val tags: List<String> = emptyList()
)