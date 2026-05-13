package com.wholesale.manager.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF1565C0),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD6E4FF),
    secondary = Color(0xFF0288D1),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCCE5FF),
    background = Color(0xFFF5F7FA),
    surface = Color.White,
    onBackground = Color(0xFF1A1C1E),
    onSurface = Color(0xFF1A1C1E),
    error = Color(0xFFBA1A1A)
)

@Composable
fun WholesaleTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}
