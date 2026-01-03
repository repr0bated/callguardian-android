package com.example.callguardian.service

import android.telecom.CallScreeningService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CallGuardianCallScreeningService : CallScreeningService() {

    @Inject lateinit var notificationManager: LookupNotificationManager

    private val job = SupervisorJob()
    private val serviceScope = CoroutineScope(job + Dispatchers.IO)

    override fun onScreenCall(callDetails: android.telecom.Call.Details) {
        val handle = callDetails.handle ?: run {
            respondToCall(callDetails, android.telecom.CallResponse.Builder().build())
            return
        }
        val number = handle.schemeSpecificPart
        if (number.isNullOrBlank()) {
            respondToCall(callDetails, android.telecom.CallResponse.Builder().build())
            return
        }

        serviceScope.launch {
            val normalized = number.filter { it.isDigit() || it == '+' }
            
            // Allow all calls by default, just notify for lookup
            respondToCall(callDetails, android.telecom.CallResponse.Builder().setDisallowCall(false).build())
            
            notificationManager.notifyBlockedCall(normalized, null)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    companion object {
        private const val LOOKUP_TIMEOUT_MS = 2500L
    }
}
