package com.danylom73.rescuehelper.mvi.nearby

sealed interface NearbyIntent {
    data object StartAdvertising : NearbyIntent
    data object StartDiscovery : NearbyIntent
    data class SendMessage(val message: String) : NearbyIntent
}
