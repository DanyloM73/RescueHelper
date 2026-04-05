package com.danylom73.rescuehelper.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.danylom73.rescuehelper.R

private val monserratFontFamily = FontFamily(
    Font(R.font.montserrat_bold, FontWeight.ExtraBold),
    Font(R.font.montserrat_medium, FontWeight.Bold),
    Font(R.font.montserrat_regular, FontWeight.Normal),
    Font(R.font.montserrat_thin, FontWeight.Thin)
)

val Typography = Typography(
    bodyMedium = TextStyle(
        fontFamily = monserratFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = monserratFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    titleLarge = TextStyle(
        fontFamily = monserratFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 24.sp
    )
)