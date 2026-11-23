package com.example.callguardian.service

import android.telecom.Call
import android.telecom.CallScreeningService
import android.telecom.TelecomManager
import com.example.callguardian.screening.ScreeningEngine
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber
import javax.inject.Inject

/**
 * Bound by Telecom when Call Guardian holds ROLE_CALL_SCREENING. Must call respondToCall()
 * within ~5 s, so only the rule engine + a 1.5 s lookup run here. The on-device model reviews
 * the call afterwards when the user opens the alert.
 */
@AndroidEntryPoint
class CallGuardianCallScreeningService : CallScreeningService() {

    @Inject lateinit var engine: ScreeningEngine
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onScreenCall(details: Call.Details) {
        val startedAt = System.currentTimeMillis()
        if (details.callDirection != Call.Details.DIRECTION_INCOMING) {
            respondToCall(details, CallResponse.Builder().build())
            return
        }
        val number = details.handle?.schemeSpecificPart
        val hidden = details.handlePresentation != TelecomManager.PRESENTATION_ALLOWED
        scope.launch {
            var eventId: Long? = null
            val decision = try {
                withTimeoutOrNull(3_500) { engine.screenCall(number, hidden, startedAt) }?.also { eventId = it.second }?.first
            } catch (t: Throwable) {
                Timber.e(t, "Call screening failed"); null
            }
            val response = CallResponse.Builder().apply {
                if (decision?.reject == true) {
                    setDisallowCall(true); setRejectCall(true); setSkipCallLog(false); setSkipNotification(true)
                } else if (decision?.silence == true) {
                    setSilenceCall(true)
                }
            }.build()
            respondToCall(details, response)
            eventId?.let { runCatching { engine.afterCall(it) } }
        }
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }
}
