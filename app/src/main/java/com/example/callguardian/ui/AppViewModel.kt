package com.example.callguardian.ui

import android.Manifest
import android.app.Application
import android.app.role.RoleManager
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.callguardian.ai.DeviceModelState
import com.example.callguardian.ai.HybridScamAnalyzer
import com.example.callguardian.data.db.PhoneProfileDao
import com.example.callguardian.data.db.ScreeningEventDao
import com.example.callguardian.data.db.ScreeningEventEntity
import com.example.callguardian.data.settings.AppSettings
import com.example.callguardian.data.settings.LookupPreferences
import com.example.callguardian.data.settings.OnDeviceModelChoice
import com.example.callguardian.firebase.FirebaseBootstrap
import com.example.callguardian.data.db.ReceiptDao
import com.example.callguardian.data.db.ReceiptEntity
import com.example.callguardian.methods.DispatchResult
import com.example.callguardian.methods.MethodRegistry
import com.example.callguardian.methods.PolicyDecision
import com.example.callguardian.methods.RegisteredMethod
import com.example.callguardian.model.GuardianMethod
import com.example.callguardian.model.VerdictSource
import com.example.callguardian.schemaui.MethodHeader
import com.example.callguardian.schemaui.SchemaForm
import com.example.callguardian.screening.ScreeningEngine
import kotlinx.serialization.json.JsonObject
import com.google.firebase.ai.DownloadStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SetupState(
    val callScreeningRole: Boolean = false,
    val smsPermission: Boolean = false,
    val contactsPermission: Boolean = false,
    val notificationsPermission: Boolean = false,
    val online: Boolean = true,
) {
    val coreReady get() = callScreeningRole && smsPermission
    val missing get() = listOf(callScreeningRole, smsPermission, contactsPermission, notificationsPermission).count { !it }
}

data class DeviceModelUi(
    val state: DeviceModelState = DeviceModelState.UNKNOWN,
    val modelName: String? = null,
    val downloading: Boolean = false,
    val downloadedBytes: Long = 0,
    val totalBytes: Long = 0,
    val error: String? = null,
)

@HiltViewModel
class AppViewModel @Inject constructor(
    private val app: Application,
    private val engine: ScreeningEngine,
    private val events: ScreeningEventDao,
    private val profilesDao: PhoneProfileDao,
    private val prefs: LookupPreferences,
    private val analyzer: HybridScamAnalyzer,
    private val receiptsDao: ReceiptDao,
    val registry: MethodRegistry,
) : AndroidViewModel(app) {

    val receipts: StateFlow<List<ReceiptEntity>> = receiptsDao.observeRecent(500).stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    fun receiptsFor(eventId: Long): Flow<List<ReceiptEntity>> = receiptsDao.observeForEvent(eventId)
    fun receipt(id: Long): Flow<ReceiptEntity?> = receiptsDao.observe(id)

    private val forms = mutableMapOf<String, SchemaForm>()
    /** Schema → UI model, cached per schema file. */
    fun inputForm(m: RegisteredMethod): SchemaForm = forms.getOrPut(m.inputSchemaId) { SchemaForm.parse(m.inputSchemaJson) }
    fun outputForm(m: RegisteredMethod): SchemaForm = forms.getOrPut(m.outputSchemaId) { SchemaForm.parse(m.outputSchemaJson) }
    fun header(m: RegisteredMethod) = MethodHeader(m.id, m.spec.title, m.spec.description.orEmpty(), m.spec.capability, m.spec.execution.name, registry.version)

    fun defaultInput(e: ScreeningEventEntity, m: GuardianMethod): JsonObject = engine.defaultInput(e, m)
    suspend fun evaluate(e: ScreeningEventEntity, m: GuardianMethod, source: VerdictSource): PolicyDecision = engine.evaluate(e, m, source)

    /** User-confirmed dispatch from a schema-rendered sheet. */
    fun runMethod(eventId: Long?, m: GuardianMethod, input: JsonObject, onResult: (DispatchResult) -> Unit) = viewModelScope.launch {
        onResult(engine.runUserMethod(eventId, m, input))
    }
    fun clearReceipts() = viewModelScope.launch { receiptsDao.clear() }

    val settings: StateFlow<AppSettings> = prefs.settings.stateIn(viewModelScope, SharingStarted.Eagerly, AppSettings())
    val recent: StateFlow<List<ScreeningEventEntity>> = events.observeRecent(300).stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val listed = profilesDao.observeListed().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _setup = MutableStateFlow(SetupState())
    val setup = _setup.asStateFlow()
    private val _model = MutableStateFlow(DeviceModelUi())
    val model = _model.asStateFlow()

    val cloudConfigured get() = FirebaseBootstrap.cloudConfigured

    fun event(id: Long): Flow<ScreeningEventEntity?> = events.observe(id)

    fun refresh() {
        val ctx = app
        fun granted(p: String) = ContextCompat.checkSelfPermission(ctx, p) == PackageManager.PERMISSION_GRANTED
        val role = ctx.getSystemService(RoleManager::class.java)?.isRoleHeld(RoleManager.ROLE_CALL_SCREENING) == true
        val cm = ctx.getSystemService(ConnectivityManager::class.java)
        val online = cm?.getNetworkCapabilities(cm.activeNetwork)?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true
        _setup.value = SetupState(
            callScreeningRole = role,
            smsPermission = granted(Manifest.permission.RECEIVE_SMS),
            contactsPermission = granted(Manifest.permission.READ_CONTACTS),
            notificationsPermission = granted(Manifest.permission.POST_NOTIFICATIONS),
            online = online,
        )
        refreshModel()
        viewModelScope.launch { engine.processPending() }
    }

    fun refreshModel() = viewModelScope.launch {
        val choice = prefs.current().modelChoice
        val st = analyzer.deviceState(choice)
        val name = if (st == DeviceModelState.AVAILABLE) analyzer.deviceModelName(choice) else null
        _model.value = _model.value.copy(state = st, modelName = name, error = null)
        if (st == DeviceModelState.AVAILABLE) analyzer.warmUp(choice)
    }

    fun downloadModel() = viewModelScope.launch {
        val choice = prefs.current().modelChoice
        _model.value = _model.value.copy(downloading = true, error = null)
        runCatching {
            analyzer.download(choice).collect { s ->
                _model.value = when (s) {
                    is DownloadStatus.DownloadStarted -> _model.value.copy(totalBytes = s.bytesToDownload, state = DeviceModelState.DOWNLOADING)
                    is DownloadStatus.DownloadInProgress -> _model.value.copy(downloadedBytes = s.totalBytesDownloaded, state = DeviceModelState.DOWNLOADING)
                    is DownloadStatus.DownloadFailed -> _model.value.copy(downloading = false, error = s.exception.message ?: "Download failed")
                    else -> _model.value.copy(downloading = false)
                }
            }
        }.onFailure { _model.value = _model.value.copy(downloading = false, error = it.message) }
        _model.value = _model.value.copy(downloading = false)
        refreshModel()
    }

    fun runTest(sender: String, body: String, onCreated: (Long) -> Unit) = viewModelScope.launch {
        val id = engine.screenSms(sender.ifBlank { "+1 (555) 010-0199" }, body, isTest = true, runAi = false)
        onCreated(id)
        engine.analyzeOnDevice(id, notify = false)
    }

    fun analyze(id: Long) = viewModelScope.launch { engine.analyzeOnDevice(id) }
    fun redactedPreview(e: ScreeningEventEntity) = engine.redactedPreview(e)
    fun delete(id: Long) = viewModelScope.launch { events.delete(id) }
    fun clearHistory() = viewModelScope.launch { events.clear() }
    fun unlist(number: String) = viewModelScope.launch { engine.unlist(number) }

    fun setPrivateMode(v: Boolean) = viewModelScope.launch { prefs.setPrivateMode(v) }
    fun setModelChoice(v: OnDeviceModelChoice) = viewModelScope.launch { prefs.setModelChoice(v); refreshModel() }
    fun setAutoBlock(v: Boolean) = viewModelScope.launch { prefs.setAutoBlock(v) }
    fun setThreshold(v: Int) = viewModelScope.launch { prefs.setAutoBlockThreshold(v) }
    fun setSilence(v: Boolean) = viewModelScope.launch { prefs.setSilenceCalls(v) }
    fun saveLookup(num: String, abs: String, url: String, h: String, hv: String) = viewModelScope.launch {
        prefs.updateNumLookupKey(num); prefs.updateAbstractKey(abs); prefs.updateCustomEndpoint(url, h, hv)
    }
}
