package com.example.callguardian.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import com.example.callguardian.screening.ScreeningEngine
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber
import javax.inject.Inject

/**
 * Listens for SMS_RECEIVED (any app holding RECEIVE_SMS gets it; no default-SMS role needed).
 * The original app required the default SMS role but didn't implement the components Android
 * demands of a default SMS app, and called abortBroadcast() on a non-ordered broadcast.
 */
@AndroidEntryPoint
class SmsReceiver : BroadcastReceiver() {
    @Inject lateinit var engine: ScreeningEngine

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) return
        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent) ?: return
        if (messages.isEmpty()) return
        // A multipart SMS arrives as several PDUs; group by sender.
        val bySender = messages.groupBy { it.displayOriginatingAddress ?: it.originatingAddress }
        val pending = goAsync()
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            try {
                withTimeoutOrNull(9_000) {
                    bySender.forEach { (sender, parts) ->
                        engine.screenSms(sender, parts.joinToString("") { it.displayMessageBody ?: it.messageBody.orEmpty() })
                    }
                }
            } catch (t: Throwable) {
                Timber.e(t, "SMS screening failed")
            } finally {
                pending.finish()
            }
        }
    }
}
