package com.example.callguardian.schemaui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.callguardian.ui.components.Pill
import com.example.callguardian.ui.theme.Brand
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull

/** Registry metadata shown on the sheet header (kept separate so the renderer has no registry dependency). */
data class MethodHeader(
    val methodId: String,
    val title: String,
    val description: String,
    val capability: String,
    val execution: String, // device | cloud
    val registryVersion: String,
)

/**
 * Confirmation sheet rendered entirely from the method's input JSON Schema. Required markers, enum
 * chips, read-only bound fields, phone/number keyboards and live validation all come from the
 * schema; this composable knows nothing about any specific method.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchemaConfirmSheet(
    header: MethodHeader,
    form: SchemaForm,
    initial: JsonObject,
    confirmLabel: String = "Run ${header.methodId}",
    onDismiss: () -> Unit,
    onConfirm: (JsonObject) -> Unit,
) {
    val sheet = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var values by remember(initial) { mutableStateOf(initial) }
    val problems = form.problems(values)
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheet) {
        Column(Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp).navigationBarsPadding()) {
            MethodHeaderBlock(header, form)
            Spacer(Modifier.size(12.dp))
            form.fields.forEach { f ->
                FieldEditor(f, values[f.name], problems[f.name]) { nv ->
                    values = JsonObject(values.toMutableMap().apply { if (nv == null) remove(f.name) else put(f.name, nv) })
                }
                Spacer(Modifier.size(10.dp))
            }
            Text("Validated against ${form.id?.substringAfterLast('/') ?: "its schema"} before it runs. A receipt is saved either way.",
                style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.size(12.dp))
            Row(Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) { Text("Cancel") }
                Button(onClick = { onConfirm(values) }, enabled = problems.isEmpty(), modifier = Modifier.weight(1f)) { Text(confirmLabel, maxLines = 1) }
            }
        }
    }
}

@Composable
fun MethodHeaderBlock(header: MethodHeader, form: SchemaForm? = null) {
    Text(header.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
    Text(header.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    Spacer(Modifier.size(8.dp))
    @OptIn(ExperimentalLayoutApi::class)
    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Pill(header.methodId, MaterialTheme.colorScheme.primary)
        Pill(header.capability, MaterialTheme.colorScheme.tertiary)
        if (header.execution == "cloud") Pill("runs in cloud", Brand.Cloud, Icons.Default.Cloud) else Pill("runs on device", Brand.Device, Icons.Default.PhoneAndroid)
        Pill("registry ${header.registryVersion}", MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FieldEditor(f: SchemaField, value: JsonElement?, problem: String?, onChange: (JsonElement?) -> Unit) {
    val label = f.title + if (f.required) " *" else ""
    if (f.readOnly) {
        ReadOnlyRow(f, value, label)
        return
    }
    when (f.kind) {
        FieldKind.ENUM -> Column {
            Text(label, style = MaterialTheme.typography.labelLarge)
            f.description?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val cur = (value as? JsonPrimitive)?.contentOrNull
                f.enumValues.forEach { opt ->
                    FilterChip(selected = cur == opt, onClick = { onChange(JsonPrimitive(opt)) }, label = { Text(SchemaForm.humanizeEnum(opt)) })
                }
            }
            problem?.let { ErrorText(it) }
        }
        FieldKind.BOOLEAN -> Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(label, style = MaterialTheme.typography.labelLarge)
                f.description?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            }
            Switch(checked = (value as? JsonPrimitive)?.contentOrNull == "true", onCheckedChange = { onChange(JsonPrimitive(it)) })
        }
        FieldKind.STRING_LIST -> {
            val text = (value as? JsonArray)?.joinToString("\n") { (it as? JsonPrimitive)?.contentOrNull.orEmpty() }.orEmpty()
            OutlinedTextField(text, { s -> onChange(JsonArray(s.lines().filter { it.isNotBlank() }.map { JsonPrimitive(it) })) },
                label = { Text(label) }, supportingText = { Text(problem ?: f.description ?: "One per line") }, isError = problem != null,
                modifier = Modifier.fillMaxWidth(), minLines = 2)
        }
        else -> {
            val text = (value as? JsonPrimitive)?.contentOrNull.orEmpty()
            val keyboard = when (f.kind) {
                FieldKind.PHONE -> KeyboardType.Phone
                FieldKind.EMAIL -> KeyboardType.Email
                FieldKind.INTEGER, FieldKind.EPOCH_MILLIS, FieldKind.NUMBER -> KeyboardType.Number
                else -> KeyboardType.Text
            }
            OutlinedTextField(
                value = text,
                onValueChange = { s ->
                    onChange(when {
                        s.isEmpty() -> null
                        f.kind == FieldKind.INTEGER || f.kind == FieldKind.EPOCH_MILLIS -> s.toLongOrNull()?.let { JsonPrimitive(it) } ?: JsonPrimitive(s)
                        f.kind == FieldKind.NUMBER -> s.toDoubleOrNull()?.let { JsonPrimitive(it) } ?: JsonPrimitive(s)
                        else -> JsonPrimitive(s)
                    })
                },
                label = { Text(label) },
                supportingText = {
                    val counter = f.maxLength?.let { " ${text.length}/$it" }.orEmpty()
                    Text((problem ?: f.description ?: "") + counter)
                },
                isError = problem != null,
                singleLine = f.kind != FieldKind.MULTILINE,
                minLines = if (f.kind == FieldKind.MULTILINE) 2 else 1,
                keyboardOptions = KeyboardOptions(keyboardType = keyboard),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun ReadOnlyRow(f: SchemaField, value: JsonElement?, label: String) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(SchemaForm.display(f, value), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
        }
        Icon(Icons.Default.Lock, contentDescription = "Bound by the app, not editable", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
    }
}

@Composable
private fun ErrorText(s: String) = Text(s, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)

/** Read-only rendering of any object against its schema (receipt input/output, deep analysis…). */
@Composable
fun SchemaDetails(form: SchemaForm, value: JsonObject?, rawFallback: String? = null) {
    if (value == null) {
        Text(rawFallback ?: "—", style = MaterialTheme.typography.bodySmall, fontFamily = FontFamily.Monospace)
        return
    }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        form.fields.forEach { f ->
            val v = value[f.name]
            if (v == null || v is JsonNull) return@forEach
            Column {
                Text(f.title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(SchemaForm.display(f, v), style = MaterialTheme.typography.bodyMedium)
            }
        }
        // Keys the schema doesn't declare (shouldn't happen: additionalProperties=false) are still shown.
        val extra = value.keys - form.fields.map { it.name }.toSet()
        if (extra.isNotEmpty()) {
            HorizontalDivider()
            extra.forEach { k -> Text("$k: ${value[k]}", style = MaterialTheme.typography.bodySmall, fontFamily = FontFamily.Monospace) }
        }
    }
}
