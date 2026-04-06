package com.danylom73.rescuehelper.presentation.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class ExtendedColors(
    val whiteBackground: Color = WhiteBackground,
    val whiteDisabled: Color = WhiteDisabled,
    val purpleBackground: Color = PurpleLightBackground,
    val orangeBackground: Color = OrangeBackground,
    val warning: Color = YellowWarning,
    val success: Color = GreenSuccess
)

val LocalExtendedColors = staticCompositionLocalOf { ExtendedColors() }