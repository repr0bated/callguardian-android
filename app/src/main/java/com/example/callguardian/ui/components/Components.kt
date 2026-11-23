package com.example.callguardian.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Rule
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.callguardian.data.db.AiStatus
import com.example.callguardian.data.db.ScreeningEventEntity
import com.example.callguardian.model.GuardianMethod
import com.example.callguardian.model.RiskLevel
import com.example.callguardian.model.ScamCategory
import com.example.callguardian.model.VerdictSource
import com.example.callguardian.model.label
import com.example.callguardian.ui.theme.Brand
import com.example.callguardian.ui.theme.color
import java.text.DateFormat
import java.util.Date

@Composable
fun Pill(text: String, color: Color, icon: ImageVector? = null, modifier: Modifier = Modifier) {
    Row(
        modifier.clip(RoundedCornerShape(50)).background(color.copy(alpha = 0.16f))
            .border(1.dp, color.copy(alpha = 0.45f), RoundedCornerShape(50)).padding(horizontal = 9.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) { Icon(icon, null, tint = color, modifier = Modifier.size(13.dp)); Spacer(Modifier.width(4.dp)) }
        Text(text.uppercase(), color = color, style = MaterialTheme.typography.labelSmall, maxLines = 1)
    }
}

@Composable
fun RiskPill(risk: RiskLevel) = Pill(risk.label(), risk.color())

@Composable
fun SourcePill(source: VerdictSource, model: String? = null) = when (source) {
    VerdictSource.ON_DEVICE -> Pill("On-device" + (model?.let { " · ${prettyModel(it)}" } ?: ""), Brand.Device, Icons.Default.Smartphone)
    VerdictSource.IN_CLOUD -> Pill("Cloud" + (model?.let { " · $it" } ?: ""), Brand.Cloud, Icons.Default.Cloud)
    VerdictSource.RULES -> Pill("Rules", Brand.Rules, Icons.Default.Rule)
    VerdictSource.USER -> Pill("You", Brand.Indigo, Icons.Default.Person)
}

fun prettyModel(name: String): String = when {
    name.contains("v4-fast", true) -> "Gemma 4 E2B"
    name.contains("v4-full", true) -> "Gemma 4 E4B"
    name.contains("nano", true) -> name
    else -> name
}

@Composable
fun SectionCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Card(
        modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) { content() } }
}

@Composable
fun SectionTitle(text: String, modifier: Modifier = Modifier) =
    Text(text, style = MaterialTheme.typography.titleMedium, modifier = modifier.padding(top = 8.dp, bottom = 2.dp))

@Composable
fun EventRow(e: ScreeningEventEntity, onClick: () -> Unit) {
    val risk = runCatching { RiskLevel.valueOf(e.risk) }.getOrDefault(RiskLevel.SAFE)
    val source = runCatching { VerdictSource.valueOf(e.decidedBy) }.getOrDefault(VerdictSource.RULES)
    val category = runCatching { ScamCategory.valueOf(e.category) }.getOrDefault(ScamCategory.NONE)
    val action = GuardianMethod.fromAlias(e.finalAction)
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.surfaceContainer).padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(42.dp).clip(CircleShape).background(risk.color().copy(alpha = 0.18f)), contentAlignment = Alignment.Center) {
            Icon(if (e.channel == "CALL") Icons.Default.Call else Icons.Default.Sms, null, tint = risk.color())
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(e.displayName ?: e.number ?: "Hidden number", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold,
                    maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f, fill = false))
                if (e.isTest) { Spacer(Modifier.width(6.dp)); Pill("Test", MaterialTheme.colorScheme.onSurfaceVariant) }
            }
            Text(
                e.body?.replace('\n', ' ') ?: (e.lookupSummary ?: "Incoming call"),
                style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1, overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.size(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                RiskPill(risk)
                when (e.aiStatus) {
                    AiStatus.PENDING, AiStatus.RUNNING -> Pill("Analyzing", Brand.Device)
                    else -> SourcePill(source)
                }
                if (category != ScamCategory.NONE) Text(category.label(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
            }
        }
        Spacer(Modifier.width(8.dp))
        Column(horizontalAlignment = Alignment.End) {
            Text(relativeTime(e.receivedAt), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            val tag = when {
                e.userAction == "BLOCKED_BY_LIST" -> "Blocked"
                e.autoApplied && action == GuardianMethod.BLOCK_NUMBER -> "Blocked"
                e.autoApplied && action == GuardianMethod.REJECT_CALL -> "Stopped"
                e.quarantined -> "Quarantined"
                else -> null
            }
            if (tag != null) Text(tag,
                style = MaterialTheme.typography.labelSmall, color = risk.color())
        }
    }
}

fun relativeTime(ts: Long): String {
    val d = System.currentTimeMillis() - ts
    return when {
        d < 60_000 -> "now"
        d < 3_600_000 -> "${d / 60_000}m"
        d < 86_400_000 -> "${d / 3_600_000}h"
        d < 7 * 86_400_000 -> "${d / 86_400_000}d"
        else -> DateFormat.getDateInstance(DateFormat.SHORT).format(Date(ts))
    }
}

fun fullTime(ts: Long): String = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(Date(ts))
