package com.danylom73.rescuehelper.presentation.screen

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
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
        }
    )
}
