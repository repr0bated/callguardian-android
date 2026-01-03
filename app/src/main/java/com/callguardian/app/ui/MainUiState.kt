package com.callguardian.app.ui

import com.callguardian.app.data.db.PhoneInteractionEntity
import com.callguardian.app.data.db.PhoneProfileEntity
import com.callguardian.app.data.settings.LookupPreferencesState

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
