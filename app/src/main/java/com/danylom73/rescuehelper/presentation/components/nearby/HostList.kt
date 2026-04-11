package com.danylom73.rescuehelper.presentation.components.nearby

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.danylom73.rescuehelper.domain.nearby.NearbyHost
import com.danylom73.rescuehelper.presentation.ui.theme.AppTheme

@Composable
fun HostList(
    hosts: List<NearbyHost>,
    onHostChosen: (NearbyHost) -> Unit
) {
    var chosenHost by rememberSaveable { mutableStateOf<NearbyHost?>(null) }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(hosts) { index, host ->

            val isSelected = chosenHost == host
            val showBorder = chosenHost != null && isSelected

            Text(
                text = "${host.name} (${host.endpointId})",
                style = AppTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (showBorder) {
                            Modifier.border(
                                width = AppTheme.dimens.thicknessRegular,
                                color = AppTheme.colors.primary,
                                shape = RoundedCornerShape(AppTheme.dimens.radiusSmall)
                            )
                        } else Modifier
                    )
                    .padding(AppTheme.dimens.spacingSmall)
                    .clickable {
                        when {
                            isSelected -> onHostChosen(host)
                            else -> chosenHost = host
                        }
                    }
            )

            if (index != hosts.lastIndex) {
                Spacer(modifier = Modifier.height(AppTheme.dimens.spacingSmall))

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.dimens.spacingSmall),
                    thickness = AppTheme.dimens.thicknessExtraSmall,
                    color = AppTheme.colors.primary
                )

                Spacer(modifier = Modifier.height(AppTheme.dimens.spacingSmall))
            }
        }
    }
}
