package com.example.callguardian.service

import android.telecom.CallScreeningService
import com.example.callguardian.data.repository.LookupRepository
import com.example.callguardian.model.InteractionDirection
import com.example.callguardian.model.InteractionType
import com.example.callguardian.notifications.LookupNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

@AndroidEntryPoint
class CallGuardianCallScreeningService : CallScreeningService() {

    @Inject lateinit var lookupRepository: LookupRepository
    @Inject lateinit var notificationManager: LookupNotificationManager

    private val job = SupervisorJob()
    private val serviceScope = CoroutineScope(job + Dispatchers.IO)

    override fun onScreenCall(callDetails: Call.Details) {
        val handle = callDetails.handle ?: run {
            respondToCall(callDetails, CallResponse.Builder().build())
            return
        }
        val number = handle.schemeSpecificPart
        if (number.isNullOrBlank()) {
            respondToCall(callDetails, CallResponse.Builder().build())
            return
        }

        serviceScope.launch {
            val normalized = number.filter { it.isDigit() || it == '+' }
            val profile = lookupRepository.getProfile(normalized)
            val shouldBlock = profile?.shouldBlockCalls() == true

            if (shouldBlock) {
                respondToCall(callDetails, CallResponse.Builder()
                    .setDisallowCall(true)
                    .setRejectCall(true)
                    .setSkipCallLog(true)
                    .setSkipNotification(true)
                    .build()
                )
                notificationManager.notifyBlockedCall(normalized, profile)
                return@launch
            }

            val lookupOutcome = withTimeoutOrNull(LOOKUP_TIMEOUT_MS) {
                lookupRepository.performLookup(
                    phoneNumber = normalized,
                    type = InteractionType.CALL,
                    direction = InteractionDirection.INCOMING
                )
            }

            respondToCall(callDetails, CallResponse.Builder().setDisallowCall(false).build())

            notificationManager.notifyLookupResult(
                phoneNumber = normalized,
                lookupResult = lookupOutcome?.lookupResult,
                existingProfile = lookupRepository.getProfile(normalized),
                aiAssessment = lookupOutcome?.aiAssessment,
                contactInfo = lookupOutcome?.contactInfo
            )
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
