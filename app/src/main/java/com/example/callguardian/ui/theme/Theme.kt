package com.example.callguardian.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.callguardian.model.RiskLevel

object Brand {
    val Indigo = Color(0xFF4F6BFF)
    val Teal = Color(0xFF14C8A8)
    val Night = Color(0xFF0B1020)
    val NightSurface = Color(0xFF141A2E)
    val NightSurface2 = Color(0xFF1C2440)
    val Cloud = Color(0xFF7AA7FF)
    val Device = Color(0xFF14C8A8)
    val Rules = Color(0xFFB0B8D0)
}

fun RiskLevel.color(): Color = when (this) {
    RiskLevel.SAFE -> Color(0xFF2EBD6B)
    RiskLevel.LOW -> Color(0xFF8BC34A)
    RiskLevel.MEDIUM -> Color(0xFFFFB300)
    RiskLevel.HIGH -> Color(0xFFFF7A1A)
    RiskLevel.CRITICAL -> Color(0xFFFF4757)
}

private val Dark = darkColorScheme(
    primary = Brand.Indigo, onPrimary = Color.White,
    primaryContainer = Color(0xFF26346F), onPrimaryContainer = Color(0xFFDDE3FF),
    secondary = Brand.Teal, onSecondary = Color(0xFF00201A),
    secondaryContainer = Color(0xFF0E4A40), onSecondaryContainer = Color(0xFFBFF5E8),
    background = Brand.Night, onBackground = Color(0xFFE6E9F5),
    surface = Brand.Night, onSurface = Color(0xFFE6E9F5),
    surfaceVariant = Brand.NightSurface2, onSurfaceVariant = Color(0xFFB4BBD3),
    surfaceContainer = Brand.NightSurface, surfaceContainerHigh = Brand.NightSurface2,
    surfaceContainerLow = Color(0xFF11172A), surfaceContainerHighest = Color(0xFF232C4D),
    outline = Color(0xFF3A4466), outlineVariant = Color(0xFF2A3354),
    error = Color(0xFFFF5A6A),
)

private val Light = lightColorScheme(
    primary = Color(0xFF3349E6), onPrimary = Color.White,
    primaryContainer = Color(0xFFDDE2FF), onPrimaryContainer = Color(0xFF0A1A66),
    secondary = Color(0xFF00897B), secondaryContainer = Color(0xFFC6F3EA), onSecondaryContainer = Color(0xFF00201B),
    background = Color(0xFFF6F7FC), surface = Color(0xFFF6F7FC),
    surfaceContainer = Color.White, surfaceContainerHigh = Color(0xFFEEF0F8), surfaceContainerLow = Color(0xFFF9FAFE),
    surfaceVariant = Color(0xFFE6E9F4), onSurfaceVariant = Color(0xFF4A5170),
)

private val AppTypography = Typography().let { t ->
    t.copy(
        headlineLarge = t.headlineLarge.copy(fontWeight = FontWeight.Bold, letterSpacing = (-0.5).sp),
        headlineMedium = t.headlineMedium.copy(fontWeight = FontWeight.Bold),
        titleLarge = t.titleLarge.copy(fontWeight = FontWeight.SemiBold),
        titleMedium = t.titleMedium.copy(fontWeight = FontWeight.SemiBold),
        labelSmall = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.6.sp),
    )
}

@Composable
fun CallGuardianTheme(dark: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (dark) Dark else Light, typography = AppTypography, content = content)
}
