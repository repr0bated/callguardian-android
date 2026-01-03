package com.example.callguardian.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Encrypted preferences implementation using AndroidX Security's EncryptedSharedPreferences.
 * Provides secure storage for sensitive data like API keys while maintaining backward compatibility.
 */
@Singleton
class EncryptedPreferences @Inject constructor(
    @ApplicationContext context: Context
) {
    private val dataStore = context.encryptedDataStore
    private val plainTextDataStore = context.plainTextDataStore

    private object Keys {
        val numlookupKey = stringPreferencesKey("numlookup_api_key_encrypted")
        val abstractKey = stringPreferencesKey("abstract_api_key_encrypted")
        val customEndpoint = stringPreferencesKey("custom_endpoint_encrypted")
        val customHeader = stringPreferencesKey("custom_header_encrypted")
        val customHeaderValue = stringPreferencesKey("custom_header_value_encrypted")
        val huggingFaceKey = stringPreferencesKey("hugging_face_api_key_encrypted")
        val huggingFaceModel = stringPreferencesKey("hugging_face_model_id_encrypted")
        val migrationCompleted = stringPreferencesKey("migration_completed")
    }

    /**
     * Flow that provides encrypted preferences state with backward compatibility.
     * If migration hasn't been completed, it will attempt to migrate from plain text storage.
     */
    val preferencesFlow: Flow<EncryptedPreferencesState> = dataStore.data
        .catch { exception ->
            Timber.e(exception, "Error reading encrypted preferences")
            // If encrypted preferences fail, try to read from plain text as fallback
            emit(emptyPreferences())
        }
        .map { prefs ->
            // Check if migration is needed
            val needsMigration = prefs[Keys.migrationCompleted].isNullOrEmpty()
            
            if (needsMigration) {
                migrateFromPlainText()
            }

            EncryptedPreferencesState(
                numLookupApiKey = prefs[Keys.numlookupKey].orEmpty(),
                abstractApiKey = prefs[Keys.abstractKey].orEmpty(),
                customEndpoint = prefs[Keys.customEndpoint].orEmpty(),
                customHeaderName = prefs[Keys.customHeader].orEmpty(),
                customHeaderValue = prefs[Keys.customHeaderValue].orEmpty(),
                huggingFaceApiKey = prefs[Keys.huggingFaceKey].orEmpty(),
                huggingFaceModelId = prefs[Keys.huggingFaceModel].orEmpty()
            )
        }

    /**
     * Updates the NumLookup API key with encryption.
     */
    suspend fun updateNumLookupKey(value: String) {
        try {
            dataStore.edit { prefs ->
                prefs[Keys.numlookupKey] = value
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to update NumLookup API key")
            throw EncryptedPreferencesException("Failed to update NumLookup API key", e)
        }
    }

    /**
     * Updates the Abstract API key with encryption.
     */
    suspend fun updateAbstractKey(value: String) {
        try {
            dataStore.edit { prefs ->
                prefs[Keys.abstractKey] = value
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to update Abstract API key")
            throw EncryptedPreferencesException("Failed to update Abstract API key", e)
        }
    }

    /**
     * Updates custom endpoint configuration with encryption.
     */
    suspend fun updateCustomEndpoint(endpoint: String, headerName: String, headerValue: String) {
        try {
            dataStore.edit { prefs ->
                prefs[Keys.customEndpoint] = endpoint
                prefs[Keys.customHeader] = headerName
                prefs[Keys.customHeaderValue] = headerValue
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to update custom endpoint configuration")
            throw EncryptedPreferencesException("Failed to update custom endpoint configuration", e)
        }
    }

    /**
     * Updates Hugging Face credentials with encryption.
     */
    suspend fun updateHuggingFaceCredentials(apiKey: String, modelId: String) {
        try {
            dataStore.edit { prefs ->
                prefs[Keys.huggingFaceKey] = apiKey
                prefs[Keys.huggingFaceModel] = modelId
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to update Hugging Face credentials")
            throw EncryptedPreferencesException("Failed to update Hugging Face credentials", e)
        }
    }

    /**
     * Migrates data from plain text storage to encrypted storage.
     * This ensures backward compatibility for existing installations.
     */
    private suspend fun migrateFromPlainText() {
        try {
            // Read from plain text storage
            val plainTextPrefs = plainTextDataStore.data.first()
            
            // Write to encrypted storage
            dataStore.edit { encryptedPrefs ->
                plainTextPrefs[Keys.numlookupKey]?.let { encryptedPrefs[Keys.numlookupKey] = it }
                plainTextPrefs[Keys.abstractKey]?.let { encryptedPrefs[Keys.abstractKey] = it }
                plainTextPrefs[Keys.customEndpoint]?.let { encryptedPrefs[Keys.customEndpoint] = it }
                plainTextPrefs[Keys.customHeader]?.let { encryptedPrefs[Keys.customHeader] = it }
                plainTextPrefs[Keys.customHeaderValue]?.let { encryptedPrefs[Keys.customHeaderValue] = it }
                plainTextPrefs[Keys.huggingFaceKey]?.let { encryptedPrefs[Keys.huggingFaceKey] = it }
                plainTextPrefs[Keys.huggingFaceModel]?.let { encryptedPrefs[Keys.huggingFaceModel] = it }
                
                // Mark migration as completed
                encryptedPrefs[Keys.migrationCompleted] = "true"
            }
            
            Timber.d("Successfully migrated preferences from plain text to encrypted storage")
        } catch (e: Exception) {
            Timber.e(e, "Failed to migrate preferences from plain text to encrypted storage")
            // Don't throw exception here as this is a best-effort migration
        }
    }

    companion object {
        private const val ENCRYPTED_PREFERENCES_NAME = "encrypted_preferences"
        private const val PLAIN_TEXT_PREFERENCES_NAME = "lookup_preferences"
        
        private val Context.encryptedDataStore: DataStore<Preferences> by lazy {
            EncryptedDataStoreFactory.createEncryptedDataStore(this, ENCRYPTED_PREFERENCES_NAME)
        }

        private val Context.plainTextDataStore: DataStore<Preferences> by preferencesDataStore(name = PLAIN_TEXT_PREFERENCES_NAME)
    }
}

/**
 * State class for encrypted preferences.
 */
data class EncryptedPreferencesState(
    val numLookupApiKey: String,
    val abstractApiKey: String,
    val customEndpoint: String,
    val customHeaderName: String,
    val customHeaderValue: String,
    val huggingFaceApiKey: String,
    val huggingFaceModelId: String
) {
    val hasNumLookupKey: Boolean get() = numLookupApiKey.isNotBlank()
    val hasAbstractKey: Boolean get() = abstractApiKey.isNotBlank()
    val hasCustomEndpoint: Boolean get() = customEndpoint.isNotBlank()
    val hasHuggingFaceCredentials: Boolean get() = huggingFaceApiKey.isNotBlank() && huggingFaceModelId.isNotBlank()
}

/**
 * Custom exception for encrypted preferences operations.
 */
class EncryptedPreferencesException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)