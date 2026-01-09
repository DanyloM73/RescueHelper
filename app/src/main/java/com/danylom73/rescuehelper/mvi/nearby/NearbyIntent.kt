package com.danylom73.rescuehelper.mvi.nearby

sealed interface NearbyIntent {
    data object StartConnecting : NearbyIntent
    data class ConnectToHost(val endpointId: String) : NearbyIntent
    data class SendMessage(val message: String) : NearbyIntent
}
