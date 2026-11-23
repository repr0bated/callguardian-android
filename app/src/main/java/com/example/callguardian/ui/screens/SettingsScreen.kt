package com.example.callguardian.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.callguardian.BuildConfig
import com.example.callguardian.data.settings.OnDeviceModelChoice
import com.example.callguardian.firebase.AppCheckInstaller
import com.example.callguardian.ui.AppViewModel
import com.example.callguardian.ui.SetupActions
import com.example.callguardian.ui.components.SectionCard
import com.example.callguardian.ui.components.SectionTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(vm: AppViewModel, actions: SetupActions, onLists: () -> Unit) {
    val s by vm.settings.collectAsState()
    val context = LocalContext.current
    var confirmClear by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("Settings") })
        Column(Modifier.verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            SectionCard {
                SectionTitle("Privacy")
                ToggleRow("Private mode", "Message content is analyzed only on this phone (ONLY_ON_DEVICE). Cloud analysis still needs your tap and only sees redacted text.", s.privateMode, vm::setPrivateMode)
            }
            SectionCard {
                SectionTitle("On-device model")
                OnDeviceModelChoice.entries.forEach { c ->
                    Row(Modifier.fillMaxWidth().selectable(s.modelChoice == c) { vm.setModelChoice(c) }, verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(s.modelChoice == c, { vm.setModelChoice(c) })
                        Column { Text(c.title, style = MaterialTheme.typography.bodyLarge); Text(c.subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                    }
                }
            }
            SectionCard {
                SectionTitle("Protection")
                ToggleRow("Auto-run block_number", "Let guardian.call.block@v1 run without a tap when policy allows.", s.autoBlock, vm::setAutoBlock)
                Text("Minimum model confidence to block: ${s.autoBlockThreshold}%", style = MaterialTheme.typography.bodyMedium)
                Slider(s.autoBlockThreshold.toFloat(), { vm.setThreshold(it.toInt()) }, valueRange = 50f..100f, steps = 9, enabled = s.autoBlock)
                ToggleRow("Silence suspicious calls", "Allow guardian.call.reject@v1 to silence high-risk calls.", s.silenceSuspiciousCalls, vm::setSilence)
                TextButton(onClick = onLists) { Text("Blocked & allowed numbers") }
            }
            SectionCard {
                SectionTitle("Cloud & App Check")
                Text(if (vm.cloudConfigured) "Firebase configured · App Check: ${AppCheckInstaller.providerName}" else "No google-services.json in this build: cloud analysis is off.",
                    style = MaterialTheme.typography.bodyMedium)
                Text("Cloud model: ${BuildConfig.CLOUD_MODEL}", style = MaterialTheme.typography.bodySmall)
                if (BuildConfig.DEBUG) {
                    val token = remember { AppCheckInstaller.debugToken(context) }
                    Text("Debug App Check token (register under Firebase console → App Check → Manage debug tokens):", style = MaterialTheme.typography.bodySmall)
                    Text(token ?: "Not generated yet. Run a cloud analysis once, then reopen Settings.", style = MaterialTheme.typography.bodySmall, fontFamily = FontFamily.Monospace)
                    if (token != null) TextButton(onClick = {
                        context.getSystemService(ClipboardManager::class.java).setPrimaryClip(ClipData.newPlainText("App Check debug token", token))
                        Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show()
                    }) { Text("Copy token") }
                }
            }
            LookupCard(vm)
            SectionCard {
                SectionTitle("Data")
                OutlinedButton(onClick = { confirmClear = true }) { Text("Clear history and receipts") }
                TextButton(onClick = actions.openAppSettings) { Text("App info & permissions") }
            }
            SectionCard {
                SectionTitle("About")
                Text("Call Guardian ${BuildConfig.VERSION_NAME} · method registry ${vm.registry.version}", style = MaterialTheme.typography.bodyMedium)
                Text("Copyright 2026 Jeremy Hobson. Licensed under the Apache License 2.0.", style = MaterialTheme.typography.bodySmall)
                Text("github.com/repr0bated/callguardian-android", style = MaterialTheme.typography.bodySmall, fontFamily = FontFamily.Monospace)
                Text("On-device inference by Firebase AI Logic (Gemma 4 / Gemini Nano via Android AICore). Cloud analysis by Gemini via Firebase AI Logic.",
                    style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
    if (confirmClear) AlertDialog(
        onDismissRequest = { confirmClear = false },
        title = { Text("Clear everything?") }, text = { Text("Deletes screened events and method receipts on this phone.") },
        confirmButton = { TextButton(onClick = { vm.clearHistory(); vm.clearReceipts(); confirmClear = false }) { Text("Clear") } },
        dismissButton = { TextButton(onClick = { confirmClear = false }) { Text("Cancel") } },
    )
}

@Composable
private fun ToggleRow(title: String, subtitle: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().clickable { onChange(!checked) }, verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(Modifier.width(8.dp))
        Switch(checked, onChange)
    }
}

@Composable
private fun LookupCard(vm: AppViewModel) {
    val s by vm.settings.collectAsState()
    var num by remember(s.numLookupApiKey) { mutableStateOf(s.numLookupApiKey) }
    var abs by remember(s.abstractApiKey) { mutableStateOf(s.abstractApiKey) }
    var url by remember(s.customEndpoint) { mutableStateOf(s.customEndpoint) }
    var hn by remember(s.customHeaderName) { mutableStateOf(s.customHeaderName) }
    var hv by remember(s.customHeaderValue) { mutableStateOf(s.customHeaderValue) }
    SectionCard {
        SectionTitle("Caller lookup (optional)")
        Text("Reverse lookup for unknown callers. Keys stay on this phone.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        OutlinedTextField(num, { num = it }, label = { Text("NumLookupAPI key") }, singleLine = true, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
        OutlinedTextField(abs, { abs = it }, label = { Text("Abstract API key") }, singleLine = true, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
        OutlinedTextField(url, { url = it }, label = { Text("Custom endpoint ({number} placeholder)") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(hn, { hn = it }, label = { Text("Header") }, singleLine = true, modifier = Modifier.weight(1f))
            OutlinedTextField(hv, { hv = it }, label = { Text("Value") }, singleLine = true, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.weight(1f))
        }
        Button(onClick = { vm.saveLookup(num, abs, url, hn, hv) }) { Text("Save") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListsScreen(vm: AppViewModel, onBack: () -> Unit) {
    val listed by vm.listed.collectAsState()
    Column(Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("Blocked & allowed") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } })
        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (listed.isEmpty()) item { Text("No numbers yet. block_number and allow_number receipts add them here.", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            items(listed, key = { it.phoneNumber }) { p ->
                SectionCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(p.displayName ?: p.phoneNumber, style = MaterialTheme.typography.titleSmall)
                            Text(if (p.trusted) "Allowed" else "Blocked: ${p.mode.name.lowercase()}", style = MaterialTheme.typography.bodySmall)
                        }
                        TextButton(onClick = { vm.unlist(p.phoneNumber) }) { Text("Remove") }
                    }
                }
            }
        }
    }
}
