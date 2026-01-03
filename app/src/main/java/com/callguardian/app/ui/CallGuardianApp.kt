package com.callguardian.app.ui

import android.app.role.RoleManager
import android.content.Context
import android.os.Build
import android.provider.Telephony
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.window.Dialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.callguardian.app.R
import com.callguardian.app.data.db.PhoneInteractionEntity
import com.callguardian.app.data.db.PhoneProfileEntity
import com.callguardian.app.model.BlockMode
import com.callguardian.app.model.InteractionType
import com.callguardian.app.ui.theme.CallGuardianTheme
import com.callguardian.app.model.ContactInfo
import com.callguardian.app.ui.ContactSyncUiState
import com.callguardian.app.service.ContactChange
import com.callguardian.app.service.ContactInfoField
import com.callguardian.app.service.ContactSyncResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CallGuardianApp(
    viewModel: MainViewModel,
    onRequestCallScreeningRole: () -> Unit,
    onRequestSmsRole: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var hasCallScreeningRole by remember { mutableStateOf(isCallScreeningRoleHeld(context)) }
    var isDefaultSmsApp by remember { mutableStateOf(isDefaultSmsApp(context)) }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        hasCallScreeningRole = isCallScreeningRoleHeld(context)
        isDefaultSmsApp = isDefaultSmsApp(context)
    }

    CallGuardianTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.app_name)) }
                )
            }
        ) { padding ->
            MainScreen(
                modifier = Modifier.padding(padding),
                state = uiState,
                hasCallScreeningRole = hasCallScreeningRole,
                isDefaultSmsApp = isDefaultSmsApp,
                onRequestCallScreeningRole = onRequestCallScreeningRole,
                onRequestSmsRole = onRequestSmsRole,
                onUpdateBlockMode = viewModel::updateBlockMode,
                onSaveNumLookupKey = viewModel::saveNumLookupKey,
                onSaveAbstractKey = viewModel::saveAbstractKey,
                onSaveCustomEndpoint = viewModel::saveCustomEndpoint,
                onSaveHuggingFaceCredentials = viewModel::saveHuggingFaceCredentials,
                onAnalyzeContactSync = viewModel::analyzeContactSync,
                onApplyContactSyncChanges = viewModel::applyContactSyncChanges,
                onResetContactSyncState = viewModel::resetContactSyncState,
                onToggleDarkMode = viewModel::toggleDarkMode
            )
        }
    }
}

@Composable
private fun MainScreen(
    modifier: Modifier = Modifier,
    state: MainUiState,
    hasCallScreeningRole: Boolean,
    isDefaultSmsApp: Boolean,
    onRequestCallScreeningRole: () -> Unit,
    onRequestSmsRole: () -> Unit,
    onUpdateBlockMode: (String, BlockMode) -> Unit,
    onSaveNumLookupKey: (String) -> Unit,
    onSaveAbstractKey: (String) -> Unit,
    onSaveCustomEndpoint: (String, String, String) -> Unit,
    onSaveHuggingFaceCredentials: (String, String) -> Unit,
    onAnalyzeContactSync: (String) -> Unit,
    onApplyContactSyncChanges: (ContactInfo, List<ContactChange>, List<ContactInfoField>) -> Unit,
    onResetContactSyncState: () -> Unit,
    onToggleDarkMode: () -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            RoleStatusCard(
                hasCallScreeningRole = hasCallScreeningRole,
                isDefaultSmsApp = isDefaultSmsApp,
                onRequestCallScreeningRole = onRequestCallScreeningRole,
                onRequestSmsRole = onRequestSmsRole
            )
        }

        if (state.profiles.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(id = R.string.section_known_numbers),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            items(state.profiles, key = { it.phoneNumber }) { profile ->
                ProfileCard(
                    profile = profile,
                    onUpdateBlockMode = onUpdateBlockMode,
                    onAnalyzeContactSync = onAnalyzeContactSync,
                    syncState = state.contactSyncState
                )
            }
        }

        if (state.recentInteractions.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(id = R.string.section_recent_activity),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            items(state.recentInteractions) { interaction ->
                InteractionCard(interaction = interaction)
            }
        }

            item {
                SettingsCard(
                    state = state,
                    onSaveNumLookupKey = onSaveNumLookupKey,
                    onSaveAbstractKey = onSaveAbstractKey,
                    onSaveCustomEndpoint = onSaveCustomEndpoint,
                    onSaveHuggingFaceCredentials = onSaveHuggingFaceCredentials,
                    onToggleDarkMode = onToggleDarkMode
                )
            }
    }

    // Show contact sync dialog when needed
    when (state.contactSyncState) {
        is ContactSyncUiState.SyncAvailable -> {
            ContactSyncDialog(
                syncResult = state.contactSyncState.syncResult,
                onApplyChanges = onApplyContactSyncChanges,
                onDismiss = onResetContactSyncState
            )
        }
        is ContactSyncUiState.Error -> {
            // Could show an error dialog here
        }
        else -> {}
    }
}

@Composable
private fun RoleStatusCard(
    hasCallScreeningRole: Boolean,
    isDefaultSmsApp: Boolean,
    onRequestCallScreeningRole: () -> Unit,
    onRequestSmsRole: () -> Unit
) {
    Card {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = stringResource(id = R.string.card_required_roles),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            RoleRow(
                title = stringResource(id = R.string.role_call_screening),
                description = stringResource(id = R.string.role_call_screening_desc),
                isGranted = hasCallScreeningRole,
                actionLabel = stringResource(id = R.string.cta_activate),
                onAction = onRequestCallScreeningRole
            )
            RoleRow(
                title = stringResource(id = R.string.role_sms),
                description = stringResource(id = R.string.role_sms_desc),
                isGranted = isDefaultSmsApp,
                actionLabel = stringResource(id = R.string.cta_set_default),
                onAction = onRequestSmsRole
            )
        }
    }
}

@Composable
private fun RoleRow(
    title: String,
    description: String,
    isGranted: Boolean,
    actionLabel: String,
    onAction: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.SemiBold)
            Text(text = description, style = MaterialTheme.typography.bodySmall)
            Text(
                text = if (isGranted) stringResource(id = R.string.status_granted) else stringResource(id = R.string.status_required),
                color = if (isGranted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
        if (!isGranted) {
            Button(onClick = onAction) {
                Text(text = actionLabel)
            }
        }
    }
}

@Composable
private fun ProfileCard(
    profile: PhoneProfileEntity,
    onUpdateBlockMode: (String, BlockMode) -> Unit,
    onAnalyzeContactSync: (String) -> Unit,
    syncState: ContactSyncUiState
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = profile.displayName ?: profile.phoneNumber, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(text = profile.phoneNumber, style = MaterialTheme.typography.bodySmall)
            profile.carrier?.let {
                Text(text = stringResource(id = R.string.carrier_label, it), style = MaterialTheme.typography.bodySmall)
            }
            profile.region?.let {
                Text(text = stringResource(id = R.string.region_label, it), style = MaterialTheme.typography.bodySmall)
            }
            if (profile.tags.isNotEmpty()) {
                profile.tags.forEach { tag ->
                    Text(text = tag, style = MaterialTheme.typography.bodySmall)
                }
            }
            Text(
                text = stringResource(id = R.string.block_mode_label, profile.blockMode.name),
                style = MaterialTheme.typography.bodySmall
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = { onUpdateBlockMode(profile.phoneNumber, BlockMode.NONE) }) {
                    Text(text = stringResource(id = R.string.action_allow))
                }
                TextButton(onClick = { onUpdateBlockMode(profile.phoneNumber, BlockMode.CALLS) }) {
                    Text(text = stringResource(id = R.string.block_calls))
                }
                TextButton(onClick = { onUpdateBlockMode(profile.phoneNumber, BlockMode.ALL) }) {
                    Text(text = stringResource(id = R.string.block_all))
                }
            }

            // Contact sync button for existing contacts
            if (profile.isExistingContact) {
                TextButton(
                    onClick = { onAnalyzeContactSync(profile.phoneNumber) },
                    enabled = syncState !is ContactSyncUiState.Analyzing
                ) {
                    Text(text = "Sync Contact")
                }
            }
        }
    }
}

@Composable
private fun InteractionCard(interaction: PhoneInteractionEntity) {
    Card {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = interaction.phoneNumber, fontWeight = FontWeight.SemiBold)
            Text(
                text = stringResource(id = R.string.interaction_meta, interaction.type, interaction.status),
                style = MaterialTheme.typography.bodySmall
            )
            interaction.lookupSummary?.let { Text(text = it, style = MaterialTheme.typography.bodySmall) }
            if (!interaction.messageBody.isNullOrBlank() && interaction.type == InteractionType.SMS) {
                Text(text = interaction.messageBody, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun SettingsCard(
    state: MainUiState,
    onSaveNumLookupKey: (String) -> Unit,
    onSaveAbstractKey: (String) -> Unit,
    onSaveCustomEndpoint: (String, String, String) -> Unit,
    onSaveHuggingFaceCredentials: (String, String) -> Unit,
    onToggleDarkMode: () -> Unit
) {
    var numLookupKey by remember(state.preferences.numLookupApiKey) { mutableStateOf(state.preferences.numLookupApiKey) }
    var abstractKey by remember(state.preferences.abstractApiKey) { mutableStateOf(state.preferences.abstractApiKey) }
    var customEndpoint by remember(state.preferences.customEndpoint) { mutableStateOf(state.preferences.customEndpoint) }
    var customHeaderName by remember(state.preferences.customHeaderName) { mutableStateOf(state.preferences.customHeaderName) }
    var customHeaderValue by remember(state.preferences.customHeaderValue) { mutableStateOf(state.preferences.customHeaderValue) }
    var huggingFaceKey by remember(state.preferences.huggingFaceApiKey) { mutableStateOf(state.preferences.huggingFaceApiKey) }
    var huggingFaceModel by remember(state.preferences.huggingFaceModelId) { mutableStateOf(state.preferences.huggingFaceModelId) }

    Card {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = stringResource(id = R.string.lookup_services),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            LabeledTextField(
                label = stringResource(id = R.string.input_numlookup_key),
                value = numLookupKey,
                onValueChange = { numLookupKey = it }
            )
            Button(onClick = { onSaveNumLookupKey(numLookupKey) }) {
                Text(text = stringResource(id = R.string.save_numlookup_key))
            }

            LabeledTextField(
                label = stringResource(id = R.string.input_abstract_key),
                value = abstractKey,
                onValueChange = { abstractKey = it }
            )
            Button(onClick = { onSaveAbstractKey(abstractKey) }) {
                Text(text = stringResource(id = R.string.save_abstract_key))
            }

            LabeledTextField(
                label = stringResource(id = R.string.input_custom_endpoint),
                value = customEndpoint,
                onValueChange = { customEndpoint = it }
            )
            LabeledTextField(
                label = stringResource(id = R.string.input_header_name),
                value = customHeaderName,
                onValueChange = { customHeaderName = it }
            )
            LabeledTextField(
                label = stringResource(id = R.string.input_header_value),
                value = customHeaderValue,
                onValueChange = { customHeaderValue = it }
            )
            Button(onClick = { onSaveCustomEndpoint(customEndpoint, customHeaderName, customHeaderValue) }) {
                Text(text = stringResource(id = R.string.save_custom_endpoint))
            }

            LabeledTextField(
                label = stringResource(id = R.string.input_hf_api_key),
                value = huggingFaceKey,
                onValueChange = { huggingFaceKey = it }
            )
            LabeledTextField(
                label = stringResource(id = R.string.input_hf_model_id),
                value = huggingFaceModel,
                onValueChange = { huggingFaceModel = it }
            )
            Button(onClick = { onSaveHuggingFaceCredentials(huggingFaceKey, huggingFaceModel) }) {
                Text(text = stringResource(id = R.string.save_hf_credentials))
            }

            Button(onClick = onToggleDarkMode) {
                Text(text = "Toggle Dark Mode")
            }
        }
    }
}

@Composable
private fun LabeledTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    androidx.compose.material3.OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        modifier = Modifier.fillMaxWidth()
    )
}

private fun isCallScreeningRoleHeld(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val roleManager = context.getSystemService(RoleManager::class.java)
        roleManager?.isRoleHeld(RoleManager.ROLE_CALL_SCREENING) ?: false
    } else {
        false
    }
}

private fun isDefaultSmsApp(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        Telephony.Sms.getDefaultSmsPackage(context) == context.packageName
    } else {
        false
    }
}

@Composable
private fun ContactSyncDialog(
    syncResult: ContactSyncResult.ChangesDetected,
    onApplyChanges: (ContactInfo, List<ContactChange>, List<ContactInfoField>) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        androidx.compose.material3.Surface(
            shape = androidx.compose.material3.MaterialTheme.shapes.medium,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Contact Sync Available",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Contact: ${syncResult.contactInfo.displayName}",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "Phone: ${syncResult.contactInfo.phoneNumber}",
                    style = MaterialTheme.typography.bodyMedium
                )

                if (syncResult.changes.isNotEmpty()) {
                    Text(
                        text = "Proposed Changes:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    syncResult.changes.forEach { change ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = change.field,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Current: ${change.currentValue}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = "Proposed: ${change.proposedValue}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "Confidence: ${(change.confidence * 100).toInt()}%",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = when {
                                        change.confidence >= 0.8 -> MaterialTheme.colorScheme.primary
                                        change.confidence >= 0.6 -> MaterialTheme.colorScheme.secondary
                                        else -> MaterialTheme.colorScheme.error
                                    }
                                )
                            }
                        }
                    }
                }

                if (syncResult.newInfo.isNotEmpty()) {
                    Text(
                        text = "New Information:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    syncResult.newInfo.forEach { info ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = info.field,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = info.value,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Cancel")
                    }
                    Button(
                        onClick = {
                            onApplyChanges(
                                syncResult.contactInfo,
                                syncResult.changes,
                                syncResult.newInfo
                            )
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Apply Changes")
                    }
                }
            }
        }
    }
}
