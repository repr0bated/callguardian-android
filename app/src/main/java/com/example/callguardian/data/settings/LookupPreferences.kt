package com.example.callguardian.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.lookupDataStore: DataStore<Preferences> by preferencesDataStore(name = "lookup_preferences")

@Singleton
class LookupPreferences @Inject constructor(
    @ApplicationContext context: Context
) {
    private val dataStore = context.lookupDataStore

    private object Keys {
        val numlookupKey = preferencesKey<String>("numlookup_api_key")
        val abstractKey = preferencesKey<String>("abstract_api_key")
        val customEndpoint = preferencesKey<String>("custom_endpoint")
        val customHeader = preferencesKey<String>("custom_header")
        val customHeaderValue = preferencesKey<String>("custom_header_value")
        val huggingFaceKey = preferencesKey<String>("hugging_face_api_key")
        val huggingFaceModel = preferencesKey<String>("hugging_face_model_id")
    }

    val preferencesFlow: Flow<LookupPreferencesState> = dataStore.data.map { prefs ->
        LookupPreferencesState(
            numLookupApiKey = prefs[Keys.numlookupKey].orEmpty(),
            abstractApiKey = prefs[Keys.abstractKey].orEmpty(),
            customEndpoint = prefs[Keys.customEndpoint].orEmpty(),
            customHeaderName = prefs[Keys.customHeader].orEmpty(),
            customHeaderValue = prefs[Keys.customHeaderValue].orEmpty(),
            huggingFaceApiKey = prefs[Keys.huggingFaceKey].orEmpty(),
            huggingFaceModelId = prefs[Keys.huggingFaceModel].orEmpty()
        )
    }

    suspend fun updateNumLookupKey(value: String) {
        dataStore.edit { it[Keys.numlookupKey] = value }
    }

    suspend fun updateAbstractKey(value: String) {
        dataStore.edit { it[Keys.abstractKey] = value }
    }

    suspend fun updateCustomEndpoint(endpoint: String, headerName: String, headerValue: String) {
        dataStore.edit {
            it[Keys.customEndpoint] = endpoint
            it[Keys.customHeader] = headerName
            it[Keys.customHeaderValue] = headerValue
        }
    }

    suspend fun updateHuggingFaceCredentials(apiKey: String, modelId: String) {
        dataStore.edit {
            it[Keys.huggingFaceKey] = apiKey
            it[Keys.huggingFaceModel] = modelId
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
