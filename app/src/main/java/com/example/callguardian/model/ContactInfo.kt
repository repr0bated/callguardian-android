package com.example.callguardian.model

data class ContactInfo(
    val contactId: Long,
    val displayName: String,
    val phoneNumber: String,
    val photoUri: String? = null,
    val existsInContacts: Boolean = false
) {
    val isExistingContact: Boolean
        get() = existsInContacts
}

