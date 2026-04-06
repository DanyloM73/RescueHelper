package com.danylom73.rescuehelper.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val DarkColorScheme = darkColorScheme(
    primary = WhitePrimary,
    onPrimary = PurplePrimary,
    secondary = PurpleLight,
    onSecondary = PurplePrimary,
    background = PurplePrimary,
    onBackground = WhitePrimary,
    surface = WhiteBackground,
    onSurface = WhitePrimary,
    error = RedError,
    onError = WhitePrimary
)

private val LightColorScheme = lightColorScheme(
    primary = WhitePrimary,
    onPrimary = PurplePrimary,
    secondary = PurpleLight,
    onSecondary = PurplePrimary,
    background = PurplePrimary,
    onBackground = WhitePrimary,
    surface = WhiteBackground,
    onSurface = WhitePrimary,
    error = RedError,
    onError = WhitePrimary
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
    val extendedColors = ExtendedColors()

    CompositionLocalProvider(
        LocalDimens provides dimens,
        LocalExtendedColors provides extendedColors
    ) {
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

    val extendedColors: ExtendedColors
        @Composable
        get() = LocalExtendedColors.current

    val typography
        @Composable
        get() = MaterialTheme.typography
}