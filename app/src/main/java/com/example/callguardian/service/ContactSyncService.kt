package com.example.callguardian.service

import android.content.ContentProviderOperation
import android.content.Context
import android.os.Parcelable
import android.provider.ContactsContract
import com.example.callguardian.model.ContactInfo
import com.example.callguardian.model.LookupOutcome
import com.example.callguardian.model.LookupResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
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
                    type = ContactChangeType.DISPLAY_NAME,
                    field = "Display Name",
                    currentValue = existingContact.displayName ?: phoneNumber,
                    proposedValue = lookupResult.displayName!!,
                    confidence = calculateNameConfidence(lookupResult, existingContact)
                )
            )
        }

        // Check for additional information that could be added
        val newInfo = mutableListOf<ContactInfoField>()

        if (existingContact.carrier.isNullOrBlank() && !lookupResult.carrier.isNullOrBlank()) {
            newInfo.add(
                ContactInfoField(
                    field = "Carrier",
                    value = lookupResult.carrier!!,
                    category = ContactFieldCategory.CARRIER
                )
            )
        }

        if (existingContact.region.isNullOrBlank() && !lookupResult.region.isNullOrBlank()) {
            newInfo.add(
                ContactInfoField(
                    field = "Region",
                    value = lookupResult.region!!,
                    category = ContactFieldCategory.REGION
                )
            )
        }

        // Check for tags/notes that could be added
        val newTags = lookupResult.tags.filter { tag ->
            !existingContact.tags.contains(tag)
        }

        if (newTags.isNotEmpty()) {
            changes.add(
                ContactChange(
                    type = ContactChangeType.TAGS,
                    field = "Tags",
                    currentValue = existingContact.tags.joinToString(", "),
                    proposedValue = (existingContact.tags + newTags).joinToString(", "),
                    confidence = 0.8 // High confidence for tags from lookup
                )
            )
        }

        return@withContext when {
            changes.isNotEmpty() || newInfo.isNotEmpty() ->
                ContactSyncResult.ChangesDetected(changes, newInfo, existingContact, contactInfo)
            else ->
                ContactSyncResult.NoChanges
        }
    }

    /**
     * Applies approved changes to a contact
     */
    suspend fun applyContactChanges(
        contactInfo: ContactInfo,
        approvedChanges: List<ContactChange>,
        newInfo: List<ContactInfoField>
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val operations = ArrayList<ContentProviderOperation>()

            // Build update operations for approved changes
            approvedChanges.forEach { change ->
                when (change.type) {
                    ContactChangeType.DISPLAY_NAME -> {
                        operations.add(
                            ContentProviderOperation.newUpdate(
                                ContactsContract.Data.CONTENT_URI
                            )
                                .withSelection(
                                    "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
                                    arrayOf(contactInfo.contactId.toString(), ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                                )
                                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, change.proposedValue)
                                .build()
                        )
                    }
                    ContactChangeType.TAGS -> {
                        // For tags, we'll add them as notes or organization
                        val tagsText = change.proposedValue.split(", ").joinToString("\n")
                        operations.add(
                            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                .withValue(ContactsContract.Data.CONTACT_ID, contactInfo.contactId)
                                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)
                                .withValue(ContactsContract.CommonDataKinds.Note.NOTE, "CallGuardian Tags: $tagsText")
                                .build()
                        )
                    }
                    else -> {
                        // Handle other types or ignore
                    }
                }
            }

            // Add new information as notes
            if (newInfo.isNotEmpty()) {
                val infoText = newInfo.joinToString("\n") { "${it.field}: ${it.value}" }
                operations.add(
                    ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValue(ContactsContract.Data.CONTACT_ID, contactInfo.contactId)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Note.NOTE, "CallGuardian Info: $infoText")
                        .build()
                )
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

    private suspend fun getExistingContactDetails(contactId: Long): ExistingContactDetails {
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

@Parcelize
data class ExistingContactDetails(
    val displayName: String? = null,
    val phoneNumber: String? = null,
    val carrier: String? = null,
    val region: String? = null,
    val tags: List<String> = emptyList()
) : Parcelable

@Parcelize
data class ContactChange(
    val type: ContactChangeType,
    val field: String,
    val currentValue: String,
    val proposedValue: String,
    val confidence: Double
) : Parcelable

enum class ContactChangeType {
    DISPLAY_NAME, TAGS, CARRIER, REGION
}

@Parcelize
data class ContactInfoField(
    val field: String,
    val value: String,
    val category: ContactFieldCategory
) : Parcelable

enum class ContactFieldCategory {
    CARRIER, REGION, LINE_TYPE, SPAM_SCORE
}

sealed class ContactSyncResult : Parcelable {
    @Parcelize
    object NoChanges : ContactSyncResult()
    
    @Parcelize
    data class ChangesDetected(
        val changes: List<ContactChange>,
        val newInfo: List<ContactInfoField>,
        val existingContact: ExistingContactDetails,
        val contactInfo: @RawValue ContactInfo
    ) : ContactSyncResult()
}
