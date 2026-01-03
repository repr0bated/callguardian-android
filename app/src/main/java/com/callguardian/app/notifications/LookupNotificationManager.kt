package com.callguardian.app.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.callguardian.app.MainActivity
import com.callguardian.app.R
import com.callguardian.app.data.db.PhoneProfileEntity
import com.callguardian.app.model.AiAssessment
import com.callguardian.app.model.ContactInfo
import com.callguardian.app.model.LookupResult
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LookupNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val notificationManager = NotificationManagerCompat.from(context)

    init {
        ensureChannel()
    }

    fun notifyLookupResult(
        phoneNumber: String,
        lookupResult: LookupResult?,
        existingProfile: PhoneProfileEntity?,
        aiAssessment: AiAssessment? = null,
        contactInfo: ContactInfo? = null,
        includeRejectAction: Boolean = true
    ) {
        val base = lookupResult?.summary ?: existingProfile?.displayName ?: contactInfo?.displayName ?: phoneNumber
        val aiSummary = aiAssessment?.let {
            context.getString(R.string.notification_ai_summary, it.label, it.confidencePercent)
        }
        val contactTag = contactInfo?.takeIf { it.existsInContacts }?.let { "📞 Contact" }
        val content = listOfNotNull(base, aiSummary, contactTag).joinToString(separator = " • ")
        val builder = baseBuilder(phoneNumber)
            .setContentTitle(phoneNumber)
            .setContentText(content ?: phoneNumber)
            .addAction(
                0,
                context.getString(R.string.action_block),
                actionPendingIntent(NotificationActionReceiver.ACTION_BLOCK, phoneNumber)
            )
            .addAction(
                0,
                context.getString(R.string.action_allow),
                actionPendingIntent(NotificationActionReceiver.ACTION_ALLOW, phoneNumber)
            )
            .addAction(
                0,
                context.getString(R.string.action_save_contact),
                actionPendingIntent(NotificationActionReceiver.ACTION_SAVE, phoneNumber)
            )

        if (aiSummary != null && base != null) {
            builder.setStyle(
                NotificationCompat.BigTextStyle().bigText("$base\n$aiSummary")
            )
        }

        if (includeRejectAction) {
            builder.addAction(
                0,
                context.getString(R.string.action_reject_call),
                actionPendingIntent(NotificationActionReceiver.ACTION_REJECT, phoneNumber)
            )
        }

        notificationManager.notify(phoneNumber.hashCode(), builder.build())
    }

    fun notifyBlockedCall(phoneNumber: String, profile: PhoneProfileEntity?) {
        val builder = baseBuilder(phoneNumber)
            .setContentTitle("Call blocked: $phoneNumber")
            .setContentText(profile?.displayName ?: "Blocked by your rules")
            .setAutoCancel(true)
        notificationManager.notify(phoneNumber.hashCode(), builder.build())
    }

    private fun baseBuilder(phoneNumber: String): NotificationCompat.Builder {
        val contentIntent = PendingIntent.getActivity(
            context,
            phoneNumber.hashCode(),
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or mutableFlag()
        )

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(contentIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(false)
    }

    private fun actionPendingIntent(action: String, phoneNumber: String): PendingIntent {
        val intent = Intent(context, NotificationActionReceiver::class.java).apply {
            this.action = action
            putExtra(NotificationActionReceiver.EXTRA_PHONE_NUMBER, phoneNumber)
        }
        return PendingIntent.getBroadcast(
            context,
            (phoneNumber.hashCode() xor action.hashCode()),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or mutableFlag()
        )
    }

    private fun ensureChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                context.getString(R.string.notification_channel_lookup_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = context.getString(R.string.notification_channel_lookup_description)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun mutableFlag(): Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0

    companion object {
        const val CHANNEL_ID = "lookup_results"
    }
}
