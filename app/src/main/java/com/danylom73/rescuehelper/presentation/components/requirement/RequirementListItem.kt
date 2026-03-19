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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.danylom73.rescuehelper.domain.requirement.Requirement
import com.danylom73.rescuehelper.presentation.ui.theme.green
import com.danylom73.rescuehelper.presentation.ui.theme.red
import com.danylom73.rescuehelper.presentation.ui.theme.yellow

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
        Text(
            text = stringResource(requirement.type.titleRes),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.weight(1f)
        )

        Spacer(Modifier.width(16.dp))

        Box(
            modifier = Modifier
                .size(16.dp)
                .background(
                    color = when {
                        requirement.isGranted -> green
                        !requirement.isGranted && requirement.type.isOptional -> yellow
                        else -> red
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
                tint = MaterialTheme.colorScheme.background,
                modifier = Modifier.size(10.dp)
            )
        }

        if (!requirement.isGranted) {
            Spacer(Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.ArrowForwardIos,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.background,
                    modifier = Modifier.size(10.dp)
                )
            }
        }
    }

    if (spaced) {
        Spacer(Modifier.height(16.dp))
    }
}