package com.danylom73.rescuehelper.mvi.nearby

import com.danylom73.rescuehelper.domain.nearby.NearbyHost

data class NearbyState(
    val isAdvertising: Boolean = false,
    val isDiscovering: Boolean = false,
    val discoveredHosts: List<NearbyHost> = emptyList(),
    val connectedEndpointId: String? = null,
    val error: String? = null
)
