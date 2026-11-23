package com.example.callguardian.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.callguardian.MainActivity
import com.example.callguardian.R
import com.example.callguardian.data.db.ScreeningEventEntity
import com.example.callguardian.model.Channel
import com.example.callguardian.model.GuardianMethod
import com.example.callguardian.model.RiskLevel
import com.example.callguardian.model.ScamCategory
import com.example.callguardian.model.label
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlertNotifier @Inject constructor(@ApplicationContext private val context: Context) {

    private val nm = NotificationManagerCompat.from(context)

    fun ensureChannels() {
        val m = context.getSystemService(NotificationManager::class.java)
        m.createNotificationChannel(NotificationChannel(CH_ALERTS, "Scam alerts", NotificationManager.IMPORTANCE_HIGH).apply {
            description = "Warnings about suspicious calls and messages"
        })
        m.createNotificationChannel(NotificationChannel(CH_INFO, "Screening activity", NotificationManager.IMPORTANCE_LOW).apply {
            description = "Low-risk screening results and pending analysis"
        })
    }

    fun notifyEvent(e: ScreeningEventEntity, aiPending: Boolean) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) return
        val risk = runCatching { RiskLevel.valueOf(e.risk) }.getOrDefault(RiskLevel.SAFE)
        val action = GuardianMethod.fromAlias(e.finalAction)
        val blockedNow = e.autoApplied && (action == GuardianMethod.BLOCK_NUMBER || action == GuardianMethod.REJECT_CALL)
        val isCall = e.channel == Channel.CALL.name
        val who = e.displayName ?: e.number ?: "Hidden number"
        val title = when {
            e.userAction == "BLOCKED_BY_LIST" -> "Blocked ${if (isCall) "call" else "message"} from $who"
            blockedNow -> "Scam ${if (action == GuardianMethod.REJECT_CALL) "call stopped" else "blocked"}: $who"
            e.quarantined -> "Quarantined scam text from $who"
            risk >= RiskLevel.HIGH -> "Likely scam ${if (isCall) "call" else "text"} from $who"
            risk >= RiskLevel.MEDIUM -> "Suspicious ${if (isCall) "call" else "text"} from $who"
            aiPending -> "Checking ${if (isCall) "call" else "text"} from $who"
            else -> "Screened ${if (isCall) "call" else "text"} from $who"
        }
        val category = runCatching { ScamCategory.valueOf(e.category) }.getOrDefault(ScamCategory.NONE)
        val text = buildString {
            if (category != ScamCategory.NONE) append(category.label()).append(" · ")
            append(e.reasons.lineSequence().firstOrNull()?.takeIf { it.isNotBlank() } ?: risk.label() + " risk")
            if (aiPending) append("\nOpen to run the private on-device check (Gemma 4).")
            e.advice?.let { append("\n").append(it) }
        }
        val open = PendingIntent.getActivity(context, e.id.toInt(),
            Intent(context, MainActivity::class.java).putExtra(MainActivity.EXTRA_EVENT_ID, e.id)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        val b = NotificationCompat.Builder(context, if (risk >= RiskLevel.MEDIUM) CH_ALERTS else CH_INFO)
            .setSmallIcon(R.drawable.ic_notification)
            .setColor(ContextCompat.getColor(context, if (risk >= RiskLevel.HIGH) R.color.risk_high else R.color.brand))
            .setContentTitle(title)
            .setContentText(text.lineSequence().first())
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setContentIntent(open)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
        if (e.number != null && e.userAction == null) {
            if (!(action == GuardianMethod.BLOCK_NUMBER && e.autoApplied)) b.addAction(0, "Block", actionIntent(e, NotificationActionReceiver.ACTION_BLOCK))
            b.addAction(0, "Allow sender", actionIntent(e, NotificationActionReceiver.ACTION_TRUST))
        }
        runCatching { nm.notify(e.id.toInt(), b.build()) }
    }

    fun cancel(id: Long) = nm.cancel(id.toInt())

    private fun actionIntent(e: ScreeningEventEntity, action: String): PendingIntent =
        PendingIntent.getBroadcast(context, (e.id * 10 + action.length).toInt(),
            Intent(context, NotificationActionReceiver::class.java).setAction(action).putExtra(MainActivity.EXTRA_EVENT_ID, e.id),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

    companion object {
        const val CH_ALERTS = "scam_alerts"
        const val CH_INFO = "screening_info"
    }
}
