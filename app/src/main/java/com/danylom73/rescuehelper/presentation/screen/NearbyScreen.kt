package com.danylom73.rescuehelper.presentation.screen

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.danylom73.rescuehelper.domain.nearby.NearbyCommand
import com.danylom73.rescuehelper.mvi.nearby.NearbyIntent
import com.danylom73.rescuehelper.mvi.nearby.NearbySideEffect
import com.danylom73.rescuehelper.mvi.nearby.NearbyViewModel
import com.danylom73.rescuehelper.presentation.components.nearby.NearbyComposable

@Composable
fun NearbyScreen(
    modifier: Modifier = Modifier,
    viewModel: NearbyViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val isFlashlightEnabled by viewModel.isFlashlightEnabled.collectAsState()
    val isAlertEnabled by viewModel.isAlertPlaying.collectAsState()
    val context = LocalContext.current
    val config = viewModel.uiConfig

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is NearbySideEffect.ShowToast -> {
                    Toast.makeText(
                        context,
                        effect.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    LaunchedEffect(state.connectedEndpointId) {
        if (state.connectedEndpointId != null) {
            viewModel.handleIntent(
                NearbyIntent.SendCurrentFlashlightState(isFlashlightEnabled)
            )

            viewModel.handleIntent(
                NearbyIntent.SendCurrentAlertState(isAlertEnabled)
            )
        }
    }

    NearbyComposable(
        modifier = modifier,
        config = config,
        state = state,
        isFlashLightEnabled = isFlashlightEnabled,
        isAlertEnabled = isAlertEnabled,
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
