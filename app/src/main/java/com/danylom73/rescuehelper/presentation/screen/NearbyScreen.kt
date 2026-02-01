package com.danylom73.rescuehelper.presentation.screen

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
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
    val context = LocalContext.current
    val config = viewModel.uiConfig

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is NearbySideEffect.ShowToast ->
                    Toast.makeText(
                        context,
                        effect.message,
                        Toast.LENGTH_LONG
                    ).show()

                is NearbySideEffect.SetFlashlight ->
                    setFlashlight(context, effect.enabled)
            }
        }
    }

    NearbyComposable(
        modifier = modifier,
        config = config,
        state = state,
        onStartConnecting = {
            viewModel.process(NearbyIntent.StartConnecting)
        },
        onStopConnecting = {
            viewModel.process(NearbyIntent.StopConnecting)
        },
        onConnect = {
            viewModel.process(NearbyIntent.ConnectToHost(it))
        },
        onDisconnect = {
            viewModel.process(NearbyIntent.Disconnect)
        },
        onSetFlashlight = {
            viewModel.process(
                NearbyIntent.SendCommand(
                    if (it) NearbyCommand.TURN_ON_FLASHLIGHT
                    else NearbyCommand.TURN_OFF_FLASHLIGHT
                )
            )
        }
    )
}

private fun setFlashlight(context: Context, enabled: Boolean) {
    val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

    val cameraId = cameraManager.cameraIdList.firstOrNull { id ->
        cameraManager.getCameraCharacteristics(id)
            .get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
    }

    cameraId?.let {
        cameraManager.setTorchMode(it, enabled)
    }
}
