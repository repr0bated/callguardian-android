package com.callguardian.app.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import com.callguardian.app.data.repository.LookupRepository
import com.callguardian.app.model.InteractionDirection
import com.callguardian.app.model.InteractionType
import com.callguardian.app.notifications.LookupNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SmsReceiver : BroadcastReceiver() {

    @Inject lateinit var lookupRepository: LookupRepository
    @Inject lateinit var notificationManager: LookupNotificationManager

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            val originating = messages.firstOrNull()?.displayOriginatingAddress ?: return
            val body = messages.joinToString(separator = "") { it.messageBody ?: "" }

            val pendingResult = goAsync()

            scope.launch {
                val lookupOutcome = lookupRepository.performLookup(
                    phoneNumber = originating,
                    type = InteractionType.SMS,
                    direction = InteractionDirection.INCOMING,
                    messageBody = body
                )
                val profile = lookupRepository.getProfile(originating)
                if (profile?.shouldBlockMessages() == true) {
                    abortBroadcast()
                }
                notificationManager.notifyLookupResult(
                    phoneNumber = originating,
                    lookupResult = lookupOutcome.lookupResult,
                    existingProfile = profile,
                    aiAssessment = lookupOutcome.aiAssessment,
                    contactInfo = lookupOutcome.contactInfo,
                    includeRejectAction = false
                )
                pendingResult.finish()
            }
        }
    }
}
