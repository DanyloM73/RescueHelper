package com.danylom73.rescuehelper.presentation.components.requirement

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.PriorityHigh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.danylom73.rescuehelper.domain.requirement.Requirement
import com.danylom73.rescuehelper.presentation.ui.theme.AppTheme

@Composable
fun RequirementListItem(
    modifier: Modifier = Modifier,
    requirement: Requirement,
    spaced: Boolean
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = requirement.type.icon,
            contentDescription = null,
            tint = AppTheme.colors.primary,
            modifier = Modifier.size(AppTheme.dimens.iconSizeMedium)
        )

        Spacer(Modifier.width(AppTheme.dimens.spacingSmall))

        Text(
            text = stringResource(requirement.type.titleRes),
            style = AppTheme.typography.bodyMedium.copy(
                color = AppTheme.colors.primary
            ),
            modifier = Modifier.weight(1f)
        )

        Spacer(Modifier.width(AppTheme.dimens.spacingRegular))

        Box(
            modifier = Modifier
                .size(AppTheme.dimens.iconSizeRegular)
                .background(
                    color = when {
                        requirement.isGranted -> AppTheme.extendedColors.success
                        !requirement.isGranted && requirement.type.isOptional -> AppTheme.extendedColors.warning
                        else -> AppTheme.colors.error
                    },
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = when {
                    requirement.isGranted -> Icons.Outlined.Done
                    !requirement.isGranted && requirement.type.isOptional -> Icons.Outlined.PriorityHigh
                    else -> Icons.Outlined.Clear
                },
                contentDescription = null,
                tint = AppTheme.colors.background,
                modifier = Modifier.size(AppTheme.dimens.iconSizeSmall)
            )
        }

        if (!requirement.isGranted) {
            Spacer(Modifier.width(AppTheme.dimens.spacingSmallerRegular))

            Box(
                modifier = Modifier
                    .size(AppTheme.dimens.iconSizeMedium)
                    .background(AppTheme.colors.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.ArrowForwardIos,
                    contentDescription = null,
                    tint = AppTheme.colors.background,
                    modifier = Modifier.size(AppTheme.dimens.iconSizeSmall)
                )
            }
        }
    }

    if (spaced) {
        Spacer(Modifier.height(AppTheme.dimens.spacingRegular))
    }
}