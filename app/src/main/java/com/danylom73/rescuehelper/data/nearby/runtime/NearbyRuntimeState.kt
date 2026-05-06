package com.danylom73.rescuehelper.data.nearby.runtime

data class NearbyRuntimeState(
    val isRunning: Boolean = false,
    val isAdvertising: Boolean = false,
    val isDiscovering: Boolean = false,
    val connectedEndpointId: String? = null,
    val remoteFlashlightEnabled: Boolean = false,
    val remoteAlertEnabled: Boolean = false
)