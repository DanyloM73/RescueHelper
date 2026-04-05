package com.danylom73.rescuehelper.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

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

    val dimens = Dimens()

    CompositionLocalProvider(LocalDimens provides dimens) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

object AppTheme {
    val dimens: Dimens
        @Composable
        get() = LocalDimens.current

    val colors
        @Composable
        get() = MaterialTheme.colorScheme

    val typography
        @Composable
        get() = MaterialTheme.typography
}