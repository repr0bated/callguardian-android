package com.callguardian.app.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.ContactsContract
import android.telecom.TelecomManager
import androidx.core.app.NotificationManagerCompat
import com.callguardian.app.data.repository.LookupRepository
import com.callguardian.app.model.BlockMode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class NotificationActionReceiver : BroadcastReceiver() {

    @Inject lateinit var lookupRepository: LookupRepository

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        val phoneNumber = intent.getStringExtra(EXTRA_PHONE_NUMBER) ?: return

        val pendingResult = goAsync()

        scope.launch {
            NotificationManagerCompat.from(context).cancel(phoneNumber.hashCode())
            when (action) {
                ACTION_BLOCK -> lookupRepository.updateBlockMode(phoneNumber, BlockMode.ALL)
                ACTION_ALLOW -> lookupRepository.updateBlockMode(phoneNumber, BlockMode.NONE)
                ACTION_REJECT -> endCall(context)
                ACTION_SAVE -> saveContact(context, phoneNumber)
            }
            pendingResult.finish()
        }
    }

    private fun endCall(context: Context) {
        val telecomManager = context.getSystemService(TelecomManager::class.java)
        telecomManager?.endCall()
    }

    private suspend fun saveContact(context: Context, phoneNumber: String) {
        val intent = Intent(Intent.ACTION_INSERT_OR_EDIT).apply {
            type = ContactsContract.Contacts.CONTENT_ITEM_TYPE
            putExtra(ContactsContract.Intents.Insert.PHONE, phoneNumber)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        kotlinx.coroutines.withContext(Dispatchers.Main) {
            context.startActivity(intent)
        }
    }

    companion object {
        const val EXTRA_PHONE_NUMBER = "extra_phone_number"
        const val ACTION_BLOCK = "com.callguardian.app.ACTION_BLOCK"
        const val ACTION_ALLOW = "com.callguardian.app.ACTION_ALLOW"
        const val ACTION_REJECT = "com.callguardian.app.ACTION_REJECT"
        const val ACTION_SAVE = "com.callguardian.app.ACTION_SAVE"
    }
}
