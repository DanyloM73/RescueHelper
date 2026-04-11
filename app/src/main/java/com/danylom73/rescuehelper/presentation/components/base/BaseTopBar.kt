package com.danylom73.rescuehelper.presentation.components.base

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.danylom73.rescuehelper.presentation.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTopBar(
    title: String,
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = Color.Transparent
    )
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = AppTheme.typography.titleLarge.copy(
                    color = AppTheme.colors.primary
                )
            )
        },
        colors = colors
    )
}