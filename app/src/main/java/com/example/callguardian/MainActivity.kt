package com.example.callguardian

import android.Manifest
import android.app.role.RoleManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import com.example.callguardian.ui.AppViewModel
import com.example.callguardian.ui.CallGuardianApp
import com.example.callguardian.ui.SetupActions
import com.example.callguardian.ui.theme.CallGuardianTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val vm: AppViewModel by viewModels()
    private val openEvent = mutableStateOf<Long?>(null)

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { vm.refresh() }
    private val roleLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { vm.refresh() }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        handleIntent(intent)
        val actions = SetupActions(
            requestScreeningRole = ::requestScreeningRole,
            requestSms = { permissionLauncher.launch(arrayOf(Manifest.permission.RECEIVE_SMS)) },
            requestContacts = { permissionLauncher.launch(arrayOf(Manifest.permission.READ_CONTACTS)) },
            requestNotifications = {
                if (Build.VERSION.SDK_INT >= 33) permissionLauncher.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
            },
            openAppSettings = {
                startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null)))
            },
        )
        setContent {
            CallGuardianTheme {
                CallGuardianApp(vm, actions, openEvent.value, onEventOpened = { openEvent.value = null })
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        vm.refresh() // also drains on-device analysis queued while we were in the background
    }

    private fun handleIntent(i: Intent?) {
        val id = i?.getLongExtra(EXTRA_EVENT_ID, -1) ?: -1
        if (id > 0) openEvent.value = id
    }

    private fun requestScreeningRole() {
        val rm = getSystemService(RoleManager::class.java) ?: return
        if (rm.isRoleAvailable(RoleManager.ROLE_CALL_SCREENING) && !rm.isRoleHeld(RoleManager.ROLE_CALL_SCREENING)) {
            roleLauncher.launch(rm.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING))
        }
    }

    companion object {
        const val EXTRA_EVENT_ID = "event_id"
    }
}
