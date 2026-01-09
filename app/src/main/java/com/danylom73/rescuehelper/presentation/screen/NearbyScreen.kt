package com.danylom73.rescuehelper.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.danylom73.rescuehelper.mvi.nearby.NearbyIntent
import com.danylom73.rescuehelper.mvi.nearby.NearbySideEffect
import com.danylom73.rescuehelper.mvi.nearby.NearbyViewModel
import com.danylom73.rescuehelper.presentation.components.nearby.HostList

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

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(config.title)

        if (state.connectedEndpointId == null) {
            if (state.isDiscovering && config.showHosts) {
                HostList(
                    hosts = state.discoveredHosts,
                    onHostClick = {
                        viewModel.process(
                            NearbyIntent.ConnectToHost(it.endpointId)
                        )
                    }
                )
            }

            Button(onClick = { viewModel.process(NearbyIntent.StartConnecting) }) {
                Text(config.primaryButtonText)
            }
        } else {
            Text("Connected to ${state.connectedEndpointId}")

            LazyColumn {
                items(state.messages) {
                    Text(it)
                }
            }
        }
    }
}
