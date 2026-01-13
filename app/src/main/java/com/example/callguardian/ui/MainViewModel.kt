package com.example.callguardian.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.callguardian.data.db.PhoneInteractionEntity
import com.example.callguardian.data.db.PhoneProfileEntity
import com.example.callguardian.data.repository.LookupRepository
import com.example.callguardian.data.settings.LookupPreferences
import com.example.callguardian.data.settings.LookupPreferencesState
import com.example.callguardian.model.BlockMode
import com.example.callguardian.service.ContactChange
import com.example.callguardian.model.ContactInfo
import com.example.callguardian.service.ContactInfoField
import com.example.callguardian.service.ContactSyncResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ContactSyncUiState {
    object Idle : ContactSyncUiState()
    object Analyzing : ContactSyncUiState()
    object NoChanges : ContactSyncUiState()
    data class SyncAvailable(val syncResult: ContactSyncResult.ChangesDetected) : ContactSyncUiState()
    object Applying : ContactSyncUiState()
    object Success : ContactSyncUiState()
    data class Error(val message: String) : ContactSyncUiState()
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val lookupRepository: LookupRepository,
    private val lookupPreferences: LookupPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _contactSyncState = MutableStateFlow<ContactSyncUiState>(ContactSyncUiState.Idle)
    val contactSyncState: StateFlow<ContactSyncUiState> = _contactSyncState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                lookupRepository.observeProfiles(),
                lookupRepository.observeRecentInteractions(),
                lookupPreferences.preferencesFlow,
                contactSyncState
            ) { profiles, interactions, preferences, syncState ->
                MainUiState(
                    profiles = profiles,
                    recentInteractions = interactions,
                    preferences = preferences,
                    contactSyncState = syncState
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun updateBlockMode(phoneNumber: String, blockMode: BlockMode) {
        viewModelScope.launch {
            lookupRepository.updateBlockMode(phoneNumber, blockMode)
        }
    }

    fun saveNumLookupKey(value: String) {
        viewModelScope.launch {
            lookupPreferences.updateNumLookupKey(value.trim())
        }
    }

    fun saveAbstractKey(value: String) {
        viewModelScope.launch {
            lookupPreferences.updateAbstractKey(value.trim())
        }
    }

    fun saveCustomEndpoint(endpoint: String, headerName: String, headerValue: String) {
        viewModelScope.launch {
            lookupPreferences.updateCustomEndpoint(endpoint.trim(), headerName.trim(), headerValue.trim())
        }
    }

    fun saveHuggingFaceCredentials(apiKey: String, modelId: String) {
        viewModelScope.launch {
            lookupPreferences.updateHuggingFaceCredentials(apiKey.trim(), modelId.trim())
        }
    }

    fun analyzeContactSync(phoneNumber: String) {
        viewModelScope.launch {
            _contactSyncState.value = ContactSyncUiState.Analyzing

            try {
                val syncResult = lookupRepository.analyzeContactSync(phoneNumber)

                _contactSyncState.value = when (syncResult) {
                    is ContactSyncResult.NoChanges -> ContactSyncUiState.NoChanges
                    is ContactSyncResult.ChangesDetected -> ContactSyncUiState.SyncAvailable(syncResult)
                }
            } catch (e: Exception) {
                _contactSyncState.value = ContactSyncUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun applyContactSyncChanges(
        contactInfo: ContactInfo,
        approvedChanges: List<ContactChange>,
        newInfo: List<ContactInfoField>
    ) {
        viewModelScope.launch {
            _contactSyncState.value = ContactSyncUiState.Applying

            try {
                val success = lookupRepository.applyContactSyncChanges(
                    contactInfo,
                    approvedChanges,
                    newInfo
                )

                _contactSyncState.value = if (success) {
                    ContactSyncUiState.Success
                } else {
                    ContactSyncUiState.Error("Failed to apply changes")
                }
            } catch (e: Exception) {
                _contactSyncState.value = ContactSyncUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun resetContactSyncState() {
        _contactSyncState.value = ContactSyncUiState.Idle
    }

    fun toggleDarkMode() {
        // Dark mode is handled by the system and Compose's isSystemInDarkTheme()
        // This could be extended to allow manual override if needed
    }
}

data class MainUiState(
    val profiles: List<PhoneProfileEntity> = emptyList(),
    val recentInteractions: List<PhoneInteractionEntity> = emptyList(),
    val preferences: LookupPreferencesState = LookupPreferencesState("", "", "", "", "", "", ""),
    val contactSyncState: ContactSyncUiState = ContactSyncUiState.Idle
)
