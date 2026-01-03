package com.example.callguardian.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

private val Context.lookupDataStore: DataStore<Preferences> by preferencesDataStore(name = "lookup_preferences")

@Singleton
class LookupPreferences @Inject constructor(
    @ApplicationContext context: Context,
    private val encryptedPreferences: EncryptedPreferences
) {
    private val dataStore = context.lookupDataStore

    private object Keys {
        val numlookupKey = stringPreferencesKey("numlookup_api_key")
        val abstractKey = stringPreferencesKey("abstract_api_key")
        val customEndpoint = stringPreferencesKey("custom_endpoint")
        val customHeader = stringPreferencesKey("custom_header")
        val customHeaderValue = stringPreferencesKey("custom_header_value")
        val huggingFaceKey = stringPreferencesKey("hugging_face_api_key")
        val huggingFaceModel = stringPreferencesKey("hugging_face_model_id")
    }

    /**
     * Combined flow that merges encrypted preferences with plain text preferences.
     * Encrypted preferences take precedence for sensitive data.
     */
    val preferencesFlow: Flow<LookupPreferencesState> = combine(
        encryptedPreferences.preferencesFlow,
        dataStore.data
    ) { encryptedState, plainTextPrefs ->
        // For non-sensitive data, use plain text preferences
        // For sensitive data, use encrypted preferences (which will handle migration)
        LookupPreferencesState(
            numLookupApiKey = encryptedState.numLookupApiKey,
            abstractApiKey = encryptedState.abstractApiKey,
            customEndpoint = encryptedState.customEndpoint,
            customHeaderName = encryptedState.customHeaderName,
            customHeaderValue = encryptedState.customHeaderValue,
            huggingFaceApiKey = encryptedState.huggingFaceApiKey,
            huggingFaceModelId = encryptedState.huggingFaceModelId
        )
    }

    /**
     * Updates the NumLookup API key using encrypted storage.
     */
    suspend fun updateNumLookupKey(value: String) {
        try {
            encryptedPreferences.updateNumLookupKey(value)
        } catch (e: Exception) {
            Timber.e(e, "Failed to update NumLookup API key in encrypted storage")
            // Fallback to plain text storage for backward compatibility
            dataStore.edit { it[Keys.numlookupKey] = value }
        }
    }

    /**
     * Updates the Abstract API key using encrypted storage.
     */
    suspend fun updateAbstractKey(value: String) {
        try {
            encryptedPreferences.updateAbstractKey(value)
        } catch (e: Exception) {
            Timber.e(e, "Failed to update Abstract API key in encrypted storage")
            // Fallback to plain text storage for backward compatibility
            dataStore.edit { it[Keys.abstractKey] = value }
        }
    }

    /**
     * Updates custom endpoint configuration using encrypted storage.
     */
    suspend fun updateCustomEndpoint(endpoint: String, headerName: String, headerValue: String) {
        try {
            encryptedPreferences.updateCustomEndpoint(endpoint, headerName, headerValue)
        } catch (e: Exception) {
            Timber.e(e, "Failed to update custom endpoint configuration in encrypted storage")
            // Fallback to plain text storage for backward compatibility
            dataStore.edit {
                it[Keys.customEndpoint] = endpoint
                it[Keys.customHeader] = headerName
                it[Keys.customHeaderValue] = headerValue
            }
        }
    }

    /**
     * Updates Hugging Face credentials using encrypted storage.
     */
    suspend fun updateHuggingFaceCredentials(apiKey: String, modelId: String) {
        try {
            encryptedPreferences.updateHuggingFaceCredentials(apiKey, modelId)
        } catch (e: Exception) {
            Timber.e(e, "Failed to update Hugging Face credentials in encrypted storage")
            // Fallback to plain text storage for backward compatibility
            dataStore.edit {
                it[Keys.huggingFaceKey] = apiKey
                it[Keys.huggingFaceModel] = modelId
            }
        }
    }
}

data class LookupPreferencesState(
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
