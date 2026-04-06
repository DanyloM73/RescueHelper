package com.danylom73.rescuehelper.presentation.components.base

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.danylom73.rescuehelper.presentation.ui.theme.AppTheme
import com.danylom73.rescuehelper.presentation.ui.theme.RescueHelperTheme

@Composable
fun BaseButton(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isActivated: Boolean = false,
    buttonText: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor =
                if (isActivated) AppTheme.colors.primary
                else AppTheme.extendedColors.whiteBackground,
            contentColor =
                if (isActivated) AppTheme.colors.background
                else AppTheme.colors.primary,
            disabledContainerColor = AppTheme.extendedColors.whiteDisabled,
            disabledContentColor = AppTheme.extendedColors.whiteBackground
        ),
        border =
            if (isEnabled) BorderStroke(
                AppTheme.dimens.thicknessExtraSmall,
                AppTheme.colors.primary
            )
            else null
    ) {
        Text(
            text = buttonText,
            style = AppTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Preview
@Composable
fun BaseButtonPreviewDefault() {
    RescueHelperTheme {
        BaseButton(
            buttonText = "Some Text",
            onClick = {}
        )
    }
}

@Preview
@Composable
fun BaseButtonPreviewDisabled() {
    RescueHelperTheme {
        BaseButton(
            buttonText = "Some Text",
            isEnabled = false,
            onClick = {}
        )
    }
}

@Preview
@Composable
fun BaseButtonPreviewActive() {
    RescueHelperTheme {
        BaseButton(
            buttonText = "Some Text",
            isActivated = true,
            onClick = {}
        )
    }
}