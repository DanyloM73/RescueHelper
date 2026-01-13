package com.danylom73.rescuehelper.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = lightYellow,
    secondary = darkGreen,
    background = darkBlue,
    tertiary = orange
)

private val LightColorScheme = lightColorScheme(
    primary = darkGreen,
    secondary = darkBlue,
    background = lightYellow,
    tertiary = orange
)

@Composable
fun RescueHelperTheme(
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