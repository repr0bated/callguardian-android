package com.callguardian.app.ui

import com.callguardian.app.service.ContactInfo
import com.callguardian.app.service.ContactSyncResult

/**
 * UI state for contact synchronization operations.
 */
sealed class ContactSyncUiState {
    object Idle : ContactSyncUiState()
    object Analyzing : ContactSyncUiState()
    data class SyncAvailable(val syncResult: ContactSyncResult.ChangesDetected) : ContactSyncUiState()
    data class Error(val message: String) : ContactSyncUiState()
    data class Success(val result: ContactSyncResult) : ContactSyncUiState()

    val isLoading: Boolean
        get() = this is Analyzing

    val isSuccess: Boolean
        get() = this is Success

    val error: String?
        get() = if (this is Error) message else null
}