package com.example.callguardian.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.settingsStore: DataStore<Preferences> by preferencesDataStore(name = "lookup_preferences")

/** Which on-device model family AICore should pick (Firebase AI Logic `OnDeviceModelOption`). */
enum class OnDeviceModelChoice(val title: String, val subtitle: String) {
    PREVIEW_FAST("Gemma 4 E2B · Nano 4 Fast", "Fastest. Needs AICore Developer Preview."),
    PREVIEW("Gemma 4 E4B · Nano 4 Full", "Best quality. Needs AICore Developer Preview."),
    STABLE("Gemini Nano (stable)", "Whatever stable Nano your phone ships with."),
}

data class AppSettings(
    val privateMode: Boolean = true,
    val modelChoice: OnDeviceModelChoice = OnDeviceModelChoice.PREVIEW_FAST,
    val autoBlock: Boolean = true,
    val autoBlockThreshold: Int = 85,
    val silenceSuspiciousCalls: Boolean = true,
    val onboardingDone: Boolean = false,
    val numLookupApiKey: String = "",
    val abstractApiKey: String = "",
    val customEndpoint: String = "",
    val customHeaderName: String = "",
    val customHeaderValue: String = "",
) {
    val hasNumLookupKey get() = numLookupApiKey.isNotBlank()
    val hasAbstractKey get() = abstractApiKey.isNotBlank()
    val hasCustomEndpoint get() = customEndpoint.isNotBlank()
    val anyLookupConfigured get() = hasNumLookupKey || hasAbstractKey || hasCustomEndpoint
}

@Singleton
class LookupPreferences @Inject constructor(@ApplicationContext context: Context) {
    private val store = context.settingsStore

    private object K {
        val privateMode = booleanPreferencesKey("private_mode")
        val model = stringPreferencesKey("on_device_model")
        val autoBlock = booleanPreferencesKey("auto_block")
        val threshold = intPreferencesKey("auto_block_threshold")
        val silence = booleanPreferencesKey("silence_calls")
        val onboarding = booleanPreferencesKey("onboarding_done")
        val numlookup = stringPreferencesKey("numlookup_api_key")
        val abstract = stringPreferencesKey("abstract_api_key")
        val customEndpoint = stringPreferencesKey("custom_endpoint")
        val customHeader = stringPreferencesKey("custom_header")
        val customHeaderValue = stringPreferencesKey("custom_header_value")
    }

    val settings: Flow<AppSettings> = store.data.map { p ->
        AppSettings(
            privateMode = p[K.privateMode] ?: true,
            modelChoice = p[K.model]?.let { runCatching { OnDeviceModelChoice.valueOf(it) }.getOrNull() } ?: OnDeviceModelChoice.PREVIEW_FAST,
            autoBlock = p[K.autoBlock] ?: true,
            autoBlockThreshold = p[K.threshold] ?: 85,
            silenceSuspiciousCalls = p[K.silence] ?: true,
            onboardingDone = p[K.onboarding] ?: false,
            numLookupApiKey = p[K.numlookup].orEmpty(),
            abstractApiKey = p[K.abstract].orEmpty(),
            customEndpoint = p[K.customEndpoint].orEmpty(),
            customHeaderName = p[K.customHeader].orEmpty(),
            customHeaderValue = p[K.customHeaderValue].orEmpty(),
        )
    }

    /** Kept for the lookup sources, which read their keys from here. */
    val preferencesFlow: Flow<AppSettings> get() = settings

    suspend fun current(): AppSettings = settings.first()

    suspend fun setPrivateMode(v: Boolean) = store.edit { it[K.privateMode] = v }
    suspend fun setModelChoice(v: OnDeviceModelChoice) = store.edit { it[K.model] = v.name }
    suspend fun setAutoBlock(v: Boolean) = store.edit { it[K.autoBlock] = v }
    suspend fun setAutoBlockThreshold(v: Int) = store.edit { it[K.threshold] = v.coerceIn(50, 100) }
    suspend fun setSilenceCalls(v: Boolean) = store.edit { it[K.silence] = v }
    suspend fun setOnboardingDone(v: Boolean) = store.edit { it[K.onboarding] = v }
    suspend fun updateNumLookupKey(v: String) = store.edit { it[K.numlookup] = v.trim() }
    suspend fun updateAbstractKey(v: String) = store.edit { it[K.abstract] = v.trim() }
    suspend fun updateCustomEndpoint(endpoint: String, headerName: String, headerValue: String) = store.edit {
        it[K.customEndpoint] = endpoint.trim(); it[K.customHeader] = headerName.trim(); it[K.customHeaderValue] = headerValue.trim()
    }
}
