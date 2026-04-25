package com.danylom73.rescuehelper.presentation.screen

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.danylom73.rescuehelper.domain.nearby.NearbyCommand
import com.danylom73.rescuehelper.mvi.nearby.NearbyIntent
import com.danylom73.rescuehelper.mvi.nearby.NearbySideEffect
import com.danylom73.rescuehelper.mvi.nearby.NearbyViewModel
import com.danylom73.rescuehelper.presentation.components.base.BaseSnackbarVisuals
import com.danylom73.rescuehelper.presentation.components.nearby.NearbyComposable

@Composable
fun NearbyScreen(
    modifier: Modifier = Modifier,
    viewModel: NearbyViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val config = viewModel.uiConfig

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is NearbySideEffect.ShowMessage -> {
                    snackbarHostState.showSnackbar(
                        BaseSnackbarVisuals(
                            message = effect.message,
                            icon = effect.icon
                        )
                    )
                }
            }
        }
    }

    LaunchedEffect(
        state.connectedEndpointId,
        state.isLocalFlashlightEnabled,
        state.isLocalAlertEnabled
    ) {
        if (state.connectedEndpointId != null) {
            viewModel.handleIntent(
                NearbyIntent.SendCurrentFlashlightState(
                    enabled = state.isLocalFlashlightEnabled
                )
            )

            viewModel.handleIntent(
                NearbyIntent.SendCurrentAlertState(
                    enabled = state.isLocalAlertEnabled
                )
            )
        }
    }

    NearbyComposable(
        modifier = modifier,
        config = config,
        state = state,
        snackbarHostState = snackbarHostState,
        isFlashLightEnabled = state.isLocalFlashlightEnabled,
        isAlertEnabled = state.isLocalAlertEnabled,
        onStartConnecting = {
            viewModel.handleIntent(NearbyIntent.StartConnecting)
        },
        onStopConnecting = {
            viewModel.handleIntent(NearbyIntent.StopConnecting)
        },
        onConnect = {
            viewModel.handleIntent(NearbyIntent.ConnectToHost(it))
        },
        onDisconnect = {
            viewModel.handleIntent(NearbyIntent.Disconnect)
        },
        onSetFlashlight = {
            viewModel.handleIntent(
                NearbyIntent.SendCommand(
                    if (it) NearbyCommand.START_FLASHLIGHT_BLINKING
                    else NearbyCommand.STOP_FLASHLIGHT_BLINKING
                )
            )
        },
        onSetAlert = {
            viewModel.handleIntent(
                NearbyIntent.SendCommand(
                    if (it) NearbyCommand.START_ALERT
                    else NearbyCommand.STOP_ALERT
                )
            )
        }
    )
}
