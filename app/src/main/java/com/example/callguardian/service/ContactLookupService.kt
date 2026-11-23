package com.example.callguardian.service

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract
import androidx.core.content.ContextCompat
import com.example.callguardian.model.ContactInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactLookupService @Inject constructor(@ApplicationContext private val context: Context) {

    /** Uses PhoneLookup so +1 (555) 123-4567 and 5551234567 match the same contact. */
    suspend fun lookupContact(phoneNumber: String): ContactInfo? = withContext(Dispatchers.IO) {
        if (phoneNumber.isBlank() || phoneNumber.contains('@')) return@withContext null
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) return@withContext null
        val uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber))
        val projection = arrayOf(ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup.PHOTO_URI)
        runCatching {
            context.contentResolver.query(uri, projection, null, null, null)?.use { c ->
                if (!c.moveToFirst()) return@use null
                ContactInfo(
                    contactId = c.getLong(0),
                    displayName = c.getString(1) ?: phoneNumber,
                    phoneNumber = phoneNumber,
                    photoUri = c.getString(2),
                    existsInContacts = true,
                )
            }
        }.getOrNull()
    }
}
