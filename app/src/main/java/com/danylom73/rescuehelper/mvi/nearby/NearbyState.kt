package com.danylom73.rescuehelper.mvi.nearby

data class NearbyState(
    val isAdvertising: Boolean = false,
    val isDiscovering: Boolean = false,
    val connectedEndpointId: String? = null,
    val messages: List<String> = emptyList(),
    val error: String? = null
)
