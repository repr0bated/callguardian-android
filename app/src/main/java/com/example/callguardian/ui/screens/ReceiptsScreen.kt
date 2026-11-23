package com.example.callguardian.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.callguardian.data.db.ReceiptEntity
import com.example.callguardian.methods.MethodRegistry
import com.example.callguardian.schemaui.MethodHeaderBlock
import com.example.callguardian.schemaui.SchemaDetails
import com.example.callguardian.schemaui.SchemaForm
import com.example.callguardian.ui.AppViewModel
import com.example.callguardian.ui.components.Pill
import com.example.callguardian.ui.components.SectionCard
import com.example.callguardian.ui.components.SectionTitle
import com.example.callguardian.ui.components.fullTime
import com.example.callguardian.ui.components.relativeTime
import com.example.callguardian.ui.theme.Brand
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

private fun parseObj(s: String?): JsonObject? = s?.let { runCatching { MethodRegistry.json.parseToJsonElement(it).jsonObject }.getOrNull() }

@Composable
fun statusColor(s: String): Color = when (s) {
    "OK" -> Brand.Teal
    "DENIED" -> MaterialTheme.colorScheme.tertiary
    else -> MaterialTheme.colorScheme.error
}

/** List row whose summary line is derived from the method's output (or input) schema. */
@Composable
fun ReceiptRow(registry: MethodRegistry, r: ReceiptEntity, onClick: () -> Unit) {
    val reg = registry.byId(r.methodId)
    val summary = remember(r.id) {
        reg?.let {
            val out = parseObj(r.outputJson)
            if (out != null) SchemaForm.summary(SchemaForm.parse(it.outputSchemaJson), out)
            else parseObj(r.inputJson)?.let { i -> SchemaForm.summary(SchemaForm.parse(it.inputSchemaJson), i) }
        }.orEmpty()
    }
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(MaterialTheme.colorScheme.surfaceContainerHigh).clickable(onClick = onClick).padding(12.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                Pill(r.status, statusColor(r.status))
                Text(reg?.spec?.title ?: r.methodId, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            }
            Text(r.methodId, style = MaterialTheme.typography.labelSmall, fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (summary.isNotBlank()) Text(summary, style = MaterialTheme.typography.bodySmall, maxLines = 1)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(relativeTime(r.createdAt), style = MaterialTheme.typography.labelSmall)
            Text("${r.executedOn} · ${r.proposedBy.lowercase().replace('_', '-')}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptsScreen(vm: AppViewModel, onReceipt: (Long) -> Unit) {
    val receipts by vm.receipts.collectAsState()
    var filter by remember { mutableStateOf("All") }
    val shown = when (filter) {
        "Executed" -> receipts.filter { it.status == "OK" }
        "Denied" -> receipts.filter { it.status == "DENIED" }
        "Invalid" -> receipts.filter { it.status.startsWith("INVALID") || it.status == "FAILED" }
        else -> receipts
    }
    Column(Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("Receipts") })
        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                Text("Every typed method the app runs, proposes, or refuses leaves a receipt: input, schema version, where it ran, and the result. Registry ${vm.registry.version} · ${vm.registry.methods.size} methods.",
                    style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("All", "Executed", "Denied", "Invalid").forEach { f -> FilterChip(filter == f, { filter = f }, label = { Text(f) }) }
                }
            }
            if (shown.isEmpty()) item { Text("No receipts yet. Run a sample in the Lab.", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            items(shown, key = { it.id }) { r -> ReceiptRow(vm.registry, r) { onReceipt(r.id) } }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptDetailScreen(vm: AppViewModel, id: Long, onBack: () -> Unit, onEvent: (Long) -> Unit) {
    val r by vm.receipt(id).collectAsState(initial = null)
    var raw by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("Receipt #$id") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } })
        val rc = r ?: return@Column
        val reg = vm.registry.byId(rc.methodId)
        Column(Modifier.verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            if (reg != null) MethodHeaderBlock(vm.header(reg)) else Text(rc.methodId, style = MaterialTheme.typography.titleLarge)
            SectionCard {
                Row(verticalAlignment = Alignment.CenterVertically) { Pill(rc.status, statusColor(rc.status)); Spacer(Modifier.width(8.dp)); Text(fullTime(rc.createdAt), style = MaterialTheme.typography.bodySmall) }
                Meta("Dispatch", rc.dispatch.replace('_', ' ').lowercase())
                Meta("Proposed by", rc.proposedBy.replace('_', ' ').lowercase())
                Meta("Executed on", rc.executedOn)
                Meta("Registry", rc.registryVersion)
                Meta("Input schema", rc.inputSchema)
                Meta("Output schema", rc.outputSchema)
                Meta("Duration", "${rc.durationMs} ms")
                rc.eventId?.let { ev -> TextButton(onClick = { onEvent(ev) }) { Text("Open event #$ev") } }
            }
            SectionCard {
                SectionTitle("Input")
                if (reg != null) SchemaDetails(vm.inputForm(reg), parseObj(rc.inputJson), rc.inputJson) else Text(rc.inputJson, fontFamily = FontFamily.Monospace)
            }
            SectionCard {
                SectionTitle("Result")
                when {
                    rc.outputJson == null -> Text(if (rc.status == "DENIED") "Not run: refused by the capability policy." else "No output.", style = MaterialTheme.typography.bodyMedium)
                    reg != null -> SchemaDetails(vm.outputForm(reg), parseObj(rc.outputJson), rc.outputJson)
                    else -> Text(rc.outputJson, fontFamily = FontFamily.Monospace)
                }
            }
            if (rc.notes.isNotBlank()) SectionCard {
                SectionTitle("Notes")
                rc.notes.lines().filter { it.isNotBlank() }.forEach { Text("• $it", style = MaterialTheme.typography.bodySmall) }
            }
            TextButton(onClick = { raw = !raw }) { Text(if (raw) "Hide JSON" else "Show JSON") }
            if (raw) Text("input = ${rc.inputJson}\n\noutput = ${rc.outputJson}", style = MaterialTheme.typography.bodySmall, fontFamily = FontFamily.Monospace)
        }
    }
}

@Composable
private fun Meta(k: String, v: String) = Row {
    Text(k, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.width(110.dp))
    Text(v, style = MaterialTheme.typography.bodySmall, fontFamily = FontFamily.Monospace)
}
