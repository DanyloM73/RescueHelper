package com.danylom73.rescuehelper.presentation.components.requirement

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.danylom73.rescuehelper.R
import com.danylom73.rescuehelper.domain.requirement.Requirement
import com.danylom73.rescuehelper.mvi.requirement.RequirementState

@Composable
fun RequirementComposable(
    modifier: Modifier = Modifier,
    state: RequirementState,
    onRequirementClick: (Requirement) -> Unit,
    onContinueClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = stringResource(R.string.requirement_title),
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.primary
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 32.dp)
        )

        if (state.requirements.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                state.requirements.forEach { (category, requirements) ->
                    item {
                        Text(
                            text = stringResource(category.titleRes),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            thickness = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    itemsIndexed(requirements) { id, item ->
                        RequirementListItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { if (!item.isGranted) onRequirementClick(item) },
                            requirement = item,
                            spaced = id != requirements.lastIndex
                        )
                    }

                    item {
                        Spacer(Modifier.height(32.dp))
                    }
                }
            }

            Button(
                onClick = { onContinueClick() },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.requirements.values.all { list ->
                    list.all { it.isGranted || it.type.isOptional }
                },
                colors = ButtonDefaults.buttonColors().copy(
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(
                        alpha = 0.5f
                    )
                )
            ) {
                Text(
                    text = stringResource(R.string.requirement_continue_button_text),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.background
                    )
                )
            }
        }
    }
}