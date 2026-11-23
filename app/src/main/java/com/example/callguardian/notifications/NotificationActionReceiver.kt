package com.example.callguardian.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.callguardian.MainActivity
import com.example.callguardian.model.GuardianMethod
import com.example.callguardian.screening.ScreeningEngine
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationActionReceiver : BroadcastReceiver() {
    @Inject lateinit var engine: ScreeningEngine

    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getLongExtra(MainActivity.EXTRA_EVENT_ID, -1)
        if (id < 0) return
        val method = when (intent.action) { ACTION_BLOCK -> GuardianMethod.BLOCK_NUMBER; ACTION_TRUST -> GuardianMethod.ALLOW_NUMBER; else -> return }
        val pending = goAsync()
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            // A notification button is an explicit user tap: dispatched as USER_CONFIRMED, still schema-validated + receipted.
            try { engine.runUserMethodForEvent(id, method) } finally { pending.finish() }
        }
    }

    companion object {
        const val ACTION_BLOCK = "com.example.callguardian.action.BLOCK"
        const val ACTION_TRUST = "com.example.callguardian.action.TRUST"
    }
}
