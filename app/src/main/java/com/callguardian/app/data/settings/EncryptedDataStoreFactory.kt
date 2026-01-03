package com.callguardian.app.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.migrations.SharedPreferencesMigration
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Factory for creating encrypted DataStore instances using AndroidX Security.
 */
object EncryptedDataStoreFactory {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    /**
     * Creates an encrypted DataStore for the given context and name.
     */
    fun createEncryptedDataStore(
        context: Context,
        name: String
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            migrations = listOf(SharedPreferencesMigration(context, name)),
            scope = scope
        ) { context.preferencesDataStoreFile(name) }
    }
}
