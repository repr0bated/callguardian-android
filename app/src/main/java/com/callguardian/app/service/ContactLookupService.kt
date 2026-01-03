package com.callguardian.app.service

import android.content.Context
import android.provider.ContactsContract
import com.callguardian.app.service.ContactInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactLookupService @Inject constructor(
    @ApplicationContext private val context: Context
) {

    suspend fun lookupContact(phoneNumber: String): ContactInfo? = withContext(Dispatchers.IO) {
        val normalizedNumber = normalizePhoneNumber(phoneNumber)

        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
            ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER
        )

        val selection = "${ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER} = ? OR " +
                "${ContactsContract.CommonDataKinds.Phone.NUMBER} = ?"

        val selectionArgs = arrayOf(normalizedNumber, phoneNumber)

        context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val contactId = cursor.getLong(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))
                val displayName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val photoUri = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))

                ContactInfo(
                    contactId = contactId,
                    displayName = displayName,
                    phoneNumber = phoneNumber,
                    photoUri = photoUri,
                    existsInContacts = true
                )
            } else {
                null
            }
        }
    }

    private fun normalizePhoneNumber(phoneNumber: String): String {
        return phoneNumber.filter { it.isDigit() || it == '+' }
    }
}

