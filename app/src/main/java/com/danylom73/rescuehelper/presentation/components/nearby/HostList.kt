package com.danylom73.rescuehelper.presentation.components.nearby

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.danylom73.rescuehelper.domain.nearby.NearbyHost

@Composable
fun HostList(
    hosts: List<NearbyHost>,
    onHostClick: (NearbyHost) -> Unit
) {
    LazyColumn {
        items(hosts) { host ->
            ListItem(
                headlineContent = { Text(host.name) },
                supportingContent = { Text(host.endpointId) },
                modifier = Modifier.clickable {
                    onHostClick(host)
                }
            )
        }

        item {
            if (hosts.isEmpty()) {
                Text("No available hosts yet")
            }
        }
    }
}
