package com.danylom73.rescuehelper.presentation.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimens(
    val spacingExtraSmall: Dp = 4.dp,
    val spacingSmall: Dp = 8.dp,
    val spacingSmallerRegular: Dp = 12.dp,
    val spacingRegular: Dp = 16.dp,
    val spacingMedium: Dp = 24.dp,
    val spacingLarge: Dp = 32.dp,

    val thicknessExtraSmall: Dp = 1.dp,
    val thicknessSmall: Dp = 2.dp,
    val thicknessRegular: Dp = 4.dp,

    val iconSizeSmall: Dp = 10.dp,
    val iconSizeRegular: Dp = 16.dp,
    val iconSizeMedium: Dp = 20.dp,
    val iconSizeExtraHuge: Dp = 64.dp,

    val radiusSmall: Dp = 8.dp,
)

val LocalDimens = staticCompositionLocalOf { Dimens() }