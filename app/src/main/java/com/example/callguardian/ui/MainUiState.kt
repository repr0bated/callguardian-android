package com.example.callguardian.ui

import com.example.callguardian.data.db.PhoneInteractionEntity
import com.example.callguardian.data.db.PhoneProfileEntity
import com.example.callguardian.data.settings.LookupPreferencesState

/**
 * Main UI state for the CallGuardian application.
 */
data class MainUiState(
    val profiles: List<PhoneProfileEntity> = emptyList(),
    val recentInteractions: List<PhoneInteractionEntity> = emptyList(),
    val preferences: LookupPreferencesState = LookupPreferencesState(
        numLookupApiKey = "",
        abstractApiKey = "",
        customEndpoint = "",
        customHeaderName = "",
        customHeaderValue = "",
        huggingFaceApiKey = "",
        huggingFaceModelId = ""
    ),
    val contactSyncState: ContactSyncUiState = ContactSyncUiState.Idle,
    val isLoading: Boolean = false,
    val error: String? = null
)
