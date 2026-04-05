package com.danylom73.rescuehelper.presentation.components.nearby

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.danylom73.rescuehelper.R
import com.danylom73.rescuehelper.mvi.nearby.NearbyState
import com.danylom73.rescuehelper.presentation.components.base.BaseTopBar
import com.danylom73.rescuehelper.presentation.screen.config.NearbyScreenUiConfig
import com.danylom73.rescuehelper.presentation.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearbyComposable(
    modifier: Modifier = Modifier,
    config: NearbyScreenUiConfig,
    state: NearbyState,
    onStartConnecting: () -> Unit = {},
    onStopConnecting: () -> Unit = {},
    onConnect: (String) -> Unit = {},
    onDisconnect: () -> Unit = {},
    onSetFlashlight: (Boolean) -> Unit = {}
) {
    var enabledFlashlight by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            BaseTopBar(
                title = if (state.connectedEndpointId != null) {
                    stringResource(R.string.nearby_title_connected)
                } else {
                    stringResource(R.string.nearby_title_default)
                }
            )
        }
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedVisibility(
                visible = (state.isAdvertising || state.isDiscovering) && state.connectedEndpointId == null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                RadarBackground()
            }

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(AppTheme.dimens.spacingMedium)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = AppTheme.dimens.spacingRegular),
                        shape = RoundedCornerShape(AppTheme.dimens.radiusSmall),
                        colors = CardDefaults.cardColors(
                            containerColor = AppTheme.colors.primary
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(AppTheme.dimens.spacingRegular),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            when {
                                !state.connectedEndpointId.isNullOrEmpty() -> {
                                    Icon(
                                        imageVector = Icons.Filled.Wifi,
                                        contentDescription = null,
                                        tint = AppTheme.colors.background,
                                        modifier = Modifier.size(AppTheme.dimens.iconSizeExtraHuge)
                                    )
                                }

                                state.isDiscovering || state.isAdvertising -> {
                                    CyclingIcon(
                                        modifier = Modifier.size(AppTheme.dimens.iconSizeExtraHuge),
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
                                        tint = AppTheme.colors.background,
                                        modifier = Modifier.size(AppTheme.dimens.iconSizeExtraHuge)
                                    )
                                }
                            }

                            Spacer(Modifier.height(AppTheme.dimens.spacingRegular))

                            if (
                                state.isDiscovering &&
                                state.discoveredHosts.isNotEmpty() &&
                                config.showHosts
                            ) {
                                Text(
                                    text = stringResource(R.string.nearby_available_devices_text),
                                    style = AppTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = AppTheme.colors.background
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            start = AppTheme.dimens.spacingSmall,
                                            bottom = AppTheme.dimens.spacingSmall
                                        ),
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
                                            stringResource(R.string.nearby_connected_text)

                                        state.isAdvertising || state.isDiscovering ->
                                            stringResource(R.string.nearby_searching_text)

                                        else ->
                                            stringResource(R.string.nearby_no_connect_text)
                                    },
                                    style = AppTheme.typography.bodyLarge.copy(
                                        color = AppTheme.colors.background
                                    ),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }

                AnimatedVisibility(
                    state.connectedEndpointId != null && config.canDisconnect
                ) {
                    Button(
                        onClick = {
                            enabledFlashlight = !enabledFlashlight
                            onSetFlashlight(enabledFlashlight)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text =
                                if (!enabledFlashlight)
                                    stringResource(R.string.nearby_turn_on_flashlight)
                                else stringResource(R.string.nearby_turn_off_flashlight),
                            style = AppTheme.typography.bodyMedium.copy(
                                color = AppTheme.colors.background
                            )
                        )
                    }
                }

                AnimatedVisibility(
                    state.connectedEndpointId == null || config.canDisconnect
                ) {
                    Button(
                        onClick = {
                            when {
                                state.isAdvertising || state.isDiscovering -> {
                                    onStopConnecting()
                                }

                                state.connectedEndpointId != null && config.canDisconnect -> {
                                    onDisconnect()
                                }

                                else -> {
                                    onStartConnecting()
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = AppTheme.dimens.spacingSmallerRegular)
                    ) {
                        Text(
                            text = when {
                                state.isAdvertising || state.isDiscovering ->
                                    stringResource(R.string.nearby_stop_button_text)

                                state.connectedEndpointId != null && config.canDisconnect ->
                                    stringResource(R.string.nearby_disconnect_button_text)

                                else -> stringResource(R.string.nearby_start_button_text)
                            },
                            style = AppTheme.typography.bodyMedium.copy(
                                color = AppTheme.colors.background
                            )
                        )
                    }
                }
            }
        }
    }
}
