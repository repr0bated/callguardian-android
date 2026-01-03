package com.example.callguardian.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class EncryptedPreferencesTest {

    private lateinit var context: Context
    private lateinit var encryptedPreferences: EncryptedPreferences

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        encryptedPreferences = EncryptedPreferences(context)
    }

    @Test
    fun `update and retrieve NumLookup API key successfully`() = runTest {
        // Given
        val testApiKey = "test-numlookup-api-key-12345"

        // When
        encryptedPreferences.updateNumLookupKey(testApiKey)
        val preferencesState = encryptedPreferences.preferencesFlow.first()

        // Then
        assertThat(preferencesState.numLookupApiKey).isEqualTo(testApiKey)
        assertThat(preferencesState.hasNumLookupKey).isTrue()
    }

    @Test
    fun `update and retrieve Abstract API key successfully`() = runTest {
        // Given
        val testApiKey = "test-abstract-api-key-67890"

        // When
        encryptedPreferences.updateAbstractKey(testApiKey)
        val preferencesState = encryptedPreferences.preferencesFlow.first()

        // Then
        assertThat(preferencesState.abstractApiKey).isEqualTo(testApiKey)
        assertThat(preferencesState.hasAbstractKey).isTrue()
    }

    @Test
    fun `update and retrieve custom endpoint configuration successfully`() = runTest {
        // Given
        val endpoint = "https://custom.api.example.com/lookup"
        val headerName = "X-API-Key"
        val headerValue = "custom-api-secret"

        // When
        encryptedPreferences.updateCustomEndpoint(endpoint, headerName, headerValue)
        val preferencesState = encryptedPreferences.preferencesFlow.first()

        // Then
        assertThat(preferencesState.customEndpoint).isEqualTo(endpoint)
        assertThat(preferencesState.customHeaderName).isEqualTo(headerName)
        assertThat(preferencesState.customHeaderValue).isEqualTo(headerValue)
        assertThat(preferencesState.hasCustomEndpoint).isTrue()
    }

    @Test
    fun `update and retrieve Hugging Face credentials successfully`() = runTest {
        // Given
        val apiKey = "hf_test_api_key_12345"
        val modelId = "microsoft/DialoGPT-medium"

        // When
        encryptedPreferences.updateHuggingFaceCredentials(apiKey, modelId)
        val preferencesState = encryptedPreferences.preferencesFlow.first()

        // Then
        assertThat(preferencesState.huggingFaceApiKey).isEqualTo(apiKey)
        assertThat(preferencesState.huggingFaceModelId).isEqualTo(modelId)
        assertThat(preferencesState.hasHuggingFaceCredentials).isTrue()
    }

    @Test
    fun `empty preferences state has no credentials`() = runTest {
        // When
        val preferencesState = encryptedPreferences.preferencesFlow.first()

        // Then
        assertThat(preferencesState.numLookupApiKey).isEmpty()
        assertThat(preferencesState.abstractApiKey).isEmpty()
        assertThat(preferencesState.customEndpoint).isEmpty()
        assertThat(preferencesState.customHeaderName).isEmpty()
        assertThat(preferencesState.customHeaderValue).isEmpty()
        assertThat(preferencesState.huggingFaceApiKey).isEmpty()
        assertThat(preferencesState.huggingFaceModelId).isEmpty()
        
        assertThat(preferencesState.hasNumLookupKey).isFalse()
        assertThat(preferencesState.hasAbstractKey).isFalse()
        assertThat(preferencesState.hasCustomEndpoint).isFalse()
        assertThat(preferencesState.hasHuggingFaceCredentials).isFalse()
    }

    @Test
    fun `migration from plain text preserves existing data`() = runTest {
        // Given - Simulate existing plain text preferences
        val plainTextDataStore = context.plainTextDataStore
        plainTextDataStore.edit { prefs ->
            prefs[EncryptedPreferences.Keys.numlookupKey] = "existing-plain-text-key"
            prefs[EncryptedPreferences.Keys.abstractKey] = "existing-abstract-key"
        }

        // When - Access encrypted preferences (should trigger migration)
        val preferencesState = encryptedPreferences.preferencesFlow.first()

        // Then - Data should be migrated to encrypted storage
        assertThat(preferencesState.numLookupApiKey).isEqualTo("existing-plain-text-key")
        assertThat(preferencesState.abstractApiKey).isEqualTo("existing-abstract-key")
    }
}