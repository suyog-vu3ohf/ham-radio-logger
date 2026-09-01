package com.hamradio.logger.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF1F88E5),
    primaryContainer = Color(0xFFD0E4FF),
    secondary = Color(0xFF0D47A1),
    secondaryContainer = Color(0xFFBBDEFB),
    tertiary = Color(0xFFFF6F00),
    tertiaryContainer = Color(0xFFFFE0B2),
    background = Color(0xFFFAFAFA),
    surface = Color(0xFFFFFFFF),
    error = Color(0xFFB3261E)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF90CAF9),
    primaryContainer = Color(0xFF1565C0),
    secondary = Color(0xFF64B5F6),
    secondaryContainer = Color(0xFF0D47A1),
    tertiary = Color(0xFFFFB74D),
    tertiaryContainer = Color(0xFFFF6F00),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    error = Color(0xFFF9DEDC)
)

@Composable
fun HamRadioLoggerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
