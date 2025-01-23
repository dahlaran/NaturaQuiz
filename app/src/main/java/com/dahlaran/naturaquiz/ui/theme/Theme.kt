package com.dahlaran.naturaquiz.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2E7D32),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF4CAF50),
    onPrimaryContainer = Color.White,
    secondaryContainer = Color(0xFF81C784),
    onSecondaryContainer = Color.Black,
    secondary = Color(0xFF81C784),
    onSecondary = Color(0xFF1B5E20),
    background = Color(0xFFF1F8E9),
    onBackground = Color.Black,
    surface = Color(0xFF1B5E20),
    onSurface = Color.Black,
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF81C784),
    onPrimary = Color.DarkGray,
    primaryContainer = Color(0xFF2E7D32),
    onPrimaryContainer = Color.Black,
    secondary = Color(0xFF4CAF50),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF81C784),
    onSecondaryContainer = Color.Black,
    background = Color(0xFFF1F8E9),
    onBackground = Color.Black,
    surface = Color(0xFF2E7D32),
    onSurface = Color.Black,
)

/**
 * Custom theme for the whole application
 *
 * Maybe set dynamic color scheme based on system theme ?
 */
@Composable
fun NaturaQuizTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}