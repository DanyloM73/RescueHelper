package com.danylom73.rescuehelper.presentation.components.nearby

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.Wifi1Bar
import androidx.compose.material.icons.filled.Wifi2Bar
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.danylom73.rescuehelper.mvi.nearby.NearbyState
import com.danylom73.rescuehelper.presentation.screen.config.NearbyScreenUiConfig

@Composable
fun NearbyComposable(
    modifier: Modifier = Modifier,
    config: NearbyScreenUiConfig,
    state: NearbyState,
    onStartConnecting: () -> Unit = {},
    onConnect: (String) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Text(
            text = config.title,
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 24.dp, bottom = 32.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when {
                        !state.connectedEndpointId.isNullOrEmpty() -> {
                            Icon(
                                imageVector = Icons.Filled.Wifi,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.background,
                                modifier = Modifier.size(64.dp)
                            )
                        }

                        state.isDiscovering || state.isAdvertising -> {
                            CyclingIcon(
                                modifier = Modifier.size(64.dp),
                                icons = listOf(
                                    Icons.Filled.Wifi1Bar,
                                    Icons.Filled.Wifi2Bar,
                                    Icons.Filled.Wifi
                                )
                            )
                        }

                        else -> {
                            Icon(
                                imageVector = Icons.Filled.WifiOff,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.background,
                                modifier = Modifier.size(64.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    if (
                        state.isDiscovering &&
                        state.discoveredHosts.isNotEmpty() &&
                        config.showHosts
                    ) {
                        Text(
                            text = "Available hosts:",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.background
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp, bottom = 8.dp),
                            textAlign = TextAlign.Start
                        )

                        HostList(
                            hosts = state.discoveredHosts,
                            onHostChosen = { onConnect(it.endpointId) }
                        )
                    } else {
                        Text(
                            text = when {
                                !state.connectedEndpointId.isNullOrEmpty() ->
                                    "Connected to ${state.connectedEndpointId}"
                                state.isAdvertising ->
                                    "Host is available to connect"
                                else ->
                                    "No connect established"
                            },
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.background
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        Button(
            onClick = onStartConnecting,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 24.dp)
        ) {
            Text(
                text = config.primaryButtonText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.background
                )
            )
        }
    }
}
