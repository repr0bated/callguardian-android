package com.example.callguardian.lookup

import com.example.callguardian.model.LookupResult

/**
 * A single remote data source that can perform a reverse lookup.
 */
interface ReverseLookupSource {
    val id: String
    val displayName: String
    suspend fun lookup(phoneNumber: String): LookupResult?
}
