package com.example.callguardian.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "callguardian_preferences")

/**
 * DataStore preferences manager for CallGuardian application settings
 */
@Singleton
class DataStorePreferences @Inject constructor(
    private val context: Context
) {
    
    val apiKey: Flow<String?> = context.dataStore.data
        .map { it[KEY_API_KEY] }
    
    val blockMode: Flow<String?> = context.dataStore.data
        .map { it[KEY_BLOCK_MODE] }
    
    val autoScreen: Flow<String?> = context.dataStore.data
        .map { it[KEY_AUTO_SCREEN] }
    
    val lookupEnabled: Flow<String?> = context.dataStore.data
        .map { it[KEY_LOOKUP_ENABLED] }
    
    val notificationsEnabled: Flow<String?> = context.dataStore.data
        .map { it[KEY_NOTIFICATIONS_ENABLED] }
    
    val cacheDuration: Flow<Int?> = context.dataStore.data
        .map { it[KEY_CACHE_DURATION] }
    
    val maxCacheSize: Flow<Int?> = context.dataStore.data
        .map { it[KEY_MAX_CACHE_SIZE] }
    
    val firstRun: Flow<String?> = context.dataStore.data
        .map { it[KEY_FIRST_RUN] }
    
    val appVersion: Flow<String?> = context.dataStore.data
        .map { it[KEY_APP_VERSION] }
    
    val lastSync: Flow<String?> = context.dataStore.data
        .map { it[KEY_LAST_SYNC] }
    
    val premiumEnabled: Flow<String?> = context.dataStore.data
        .map { it[KEY_PREMIUM_ENABLED] }
    
    suspend fun setApiKey(apiKey: String?) {
        context.dataStore.edit { preferences ->
            if (apiKey != null) {
                preferences[KEY_API_KEY] = apiKey
            } else {
                preferences.remove(KEY_API_KEY)
            }
        }
    }
    
    suspend fun setBlockMode(blockMode: String?) {
        context.dataStore.edit { preferences ->
            if (blockMode != null) {
                preferences[KEY_BLOCK_MODE] = blockMode
            } else {
                preferences.remove(KEY_BLOCK_MODE)
            }
        }
    }
    
    suspend fun setAutoScreen(autoScreen: String?) {
        context.dataStore.edit { preferences ->
            if (autoScreen != null) {
                preferences[KEY_AUTO_SCREEN] = autoScreen
            } else {
                preferences.remove(KEY_AUTO_SCREEN)
            }
        }
    }
    
    suspend fun setLookupEnabled(lookupEnabled: String?) {
        context.dataStore.edit { preferences ->
            if (lookupEnabled != null) {
                preferences[KEY_LOOKUP_ENABLED] = lookupEnabled
            } else {
                preferences.remove(KEY_LOOKUP_ENABLED)
            }
        }
    }
    
    suspend fun setNotificationsEnabled(notificationsEnabled: String?) {
        context.dataStore.edit { preferences ->
            if (notificationsEnabled != null) {
                preferences[KEY_NOTIFICATIONS_ENABLED] = notificationsEnabled
            } else {
                preferences.remove(KEY_NOTIFICATIONS_ENABLED)
            }
        }
    }
    
    suspend fun setCacheDuration(cacheDuration: Int?) {
        context.dataStore.edit { preferences ->
            if (cacheDuration != null) {
                preferences[KEY_CACHE_DURATION] = cacheDuration
            } else {
                preferences.remove(KEY_CACHE_DURATION)
            }
        }
    }
    
    suspend fun setMaxCacheSize(maxCacheSize: Int?) {
        context.dataStore.edit { preferences ->
            if (maxCacheSize != null) {
                preferences[KEY_MAX_CACHE_SIZE] = maxCacheSize
            } else {
                preferences.remove(KEY_MAX_CACHE_SIZE)
            }
        }
    }
    
    suspend fun setFirstRun(firstRun: String?) {
        context.dataStore.edit { preferences ->
            if (firstRun != null) {
                preferences[KEY_FIRST_RUN] = firstRun
            } else {
                preferences.remove(KEY_FIRST_RUN)
            }
        }
    }
    
    suspend fun setAppVersion(appVersion: String?) {
        context.dataStore.edit { preferences ->
            if (appVersion != null) {
                preferences[KEY_APP_VERSION] = appVersion
            } else {
                preferences.remove(KEY_APP_VERSION)
            }
        }
    }
    
    suspend fun setLastSync(lastSync: String?) {
        context.dataStore.edit { preferences ->
            if (lastSync != null) {
                preferences[KEY_LAST_SYNC] = lastSync
            } else {
                preferences.remove(KEY_LAST_SYNC)
            }
        }
    }
    
    suspend fun setPremiumEnabled(premiumEnabled: String?) {
        context.dataStore.edit { preferences ->
            if (premiumEnabled != null) {
                preferences[KEY_PREMIUM_ENABLED] = premiumEnabled
            } else {
                preferences.remove(KEY_PREMIUM_ENABLED)
            }
        }
    }
    
    suspend fun clearAllPreferences() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val KEY_API_KEY = stringPreferencesKey("api_key")
        private val KEY_BLOCK_MODE = stringPreferencesKey("block_mode")
        private val KEY_AUTO_SCREEN = stringPreferencesKey("auto_screen")
        private val KEY_LOOKUP_ENABLED = stringPreferencesKey("lookup_enabled")
        private val KEY_NOTIFICATIONS_ENABLED = stringPreferencesKey("notifications_enabled")
        private val KEY_CACHE_DURATION = intPreferencesKey("cache_duration")
        private val KEY_MAX_CACHE_SIZE = intPreferencesKey("max_cache_size")
        private val KEY_FIRST_RUN = stringPreferencesKey("first_run")
        private val KEY_APP_VERSION = stringPreferencesKey("app_version")
        private val KEY_LAST_SYNC = stringPreferencesKey("last_sync")
        private val KEY_PREMIUM_ENABLED = stringPreferencesKey("premium_enabled")

        suspend fun applyMigrations(context: Context) {
            // Migration logic for existing preferences
            // This ensures backward compatibility with older versions
            val dataStore = context.dataStore
            dataStore.edit { preferences ->
                // Apply any necessary migrations here
                // Currently no migrations needed
            }
        }
    }
}