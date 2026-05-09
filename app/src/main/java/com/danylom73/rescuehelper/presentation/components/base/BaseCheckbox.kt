package com.danylom73.rescuehelper.presentation.components.base

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.danylom73.rescuehelper.presentation.ui.theme.AppTheme
import com.danylom73.rescuehelper.presentation.ui.theme.RescueHelperTheme

@Composable
fun BaseCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean,
    text: String,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { onCheckedChange(it) },
            modifier = Modifier.size(AppTheme.dimens.iconSizeMedium),
            colors = CheckboxDefaults.colors(
                checkmarkColor = AppTheme.colors.background,
                checkedColor = AppTheme.colors.primary,
                uncheckedColor = AppTheme.colors.primary
            )
        )

        Spacer(modifier = Modifier.width(AppTheme.dimens.spacingSmall))

        Text(
            text = text,
            style = AppTheme.typography.bodyMedium.copy(
                color = AppTheme.colors.primary
            )
        )
    }
}

@Preview
@Composable
fun BaseCheckboxPreviewChecked() {
    RescueHelperTheme {
        BaseCheckbox(
            checked = true,
            text = "Some text"
        ) {}
    }
}

@Preview
@Composable
fun BaseCheckboxPreviewUnchecked() {
    RescueHelperTheme {
        BaseCheckbox(
            checked = false,
            text = "Some text"
        ) {}
    }
}