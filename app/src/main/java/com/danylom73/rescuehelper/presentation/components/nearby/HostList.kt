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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.danylom73.rescuehelper.domain.nearby.NearbyHost

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
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.background
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (showBorder) {
                            Modifier.border(
                                width = 4.dp,
                                color = MaterialTheme.colorScheme.background,
                                shape = RoundedCornerShape(8.dp)
                            )
                        } else Modifier
                    )
                    .padding(8.dp)
                    .clickable {
                        when {
                            isSelected -> onHostChosen(host)
                            else -> chosenHost = host
                        }
                    }
            )

            if (index != hosts.lastIndex) {
                Spacer(modifier = Modifier.height(8.dp))

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.background
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
