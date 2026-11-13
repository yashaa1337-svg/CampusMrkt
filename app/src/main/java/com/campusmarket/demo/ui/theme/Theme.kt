package com.campusmarket.demo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Indigo600,
    onPrimary = Color.White,
    primaryContainer = Indigo100,
    onPrimaryContainer = Indigo950,
    secondary = Violet600,
    onSecondary = Color.White,
    secondaryContainer = Violet100,
    onSecondaryContainer = Violet950,
    tertiary = Lime700,
    onTertiary = Color.White,
    tertiaryContainer = Lime200,
    onTertiaryContainer = Lime950,
    background = LightBackground,
    onBackground = LightOnSurface,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFF6F5FC),
    surfaceContainer = Color(0xFFF1F0FA),
    surfaceContainerHigh = Color(0xFFEBEAF5),
    surfaceContainerHighest = Color(0xFFE5E4F0),
    outline = LightOutline,
    outlineVariant = LightOutlineVariant,
    error = Color(0xFFDC2626),
    onError = Color.White,
    errorContainer = Color(0xFFFEE2E2),
    onErrorContainer = Color(0xFF7F1D1D),
)

private val DarkColors = darkColorScheme(
    primary = Indigo300,
    onPrimary = Indigo950,
    primaryContainer = Indigo800,
    onPrimaryContainer = Indigo100,
    secondary = Violet300,
    onSecondary = Violet950,
    secondaryContainer = Violet800,
    onSecondaryContainer = Violet100,
    tertiary = Lime300,
    onTertiary = Lime950,
    tertiaryContainer = Lime800,
    onTertiaryContainer = Lime100,
    background = DarkBackground,
    onBackground = DarkOnSurface,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    surfaceContainerLowest = Color(0xFF0B0B14),
    surfaceContainerLow = Color(0xFF1A1A30),
    surfaceContainer = Color(0xFF1F1F36),
    surfaceContainerHigh = Color(0xFF26263F),
    surfaceContainerHighest = Color(0xFF2E2E4A),
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant,
    error = Color(0xFFF87171),
    onError = Color(0xFF450A0A),
    errorContainer = Color(0xFF7F1D1D),
    onErrorContainer = Color(0xFFFEE2E2),
)

@Composable
fun CampusMarketTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = AppTypography,
        shapes = AppShapes,
        content = content,
    )
}
