package com.danylom73.rescuehelper.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.danylom73.rescuehelper.mvi.nearby.NearbyIntent
import com.danylom73.rescuehelper.mvi.nearby.NearbyViewModel

@Composable
fun NearbyScreen(
    modifier: Modifier = Modifier,
    viewModel: NearbyViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(modifier = modifier.padding(16.dp)) {

        Button(onClick = { viewModel.process(NearbyIntent.StartAdvertising) }) {
            Text("Start Advertising")
        }

        Button(onClick = { viewModel.process(NearbyIntent.StartDiscovery) }) {
            Text("Start Discovery")
        }

        state.connectedEndpointId?.let {
            Text("Connected to: $it")
        }

        LazyColumn {
            items(state.messages) {
                Text(it)
            }
        }
    }
}
