package com.danylom73.rescuehelper.data.nearby.handler

import com.danylom73.rescuehelper.data.nearby.NearbyEventEmitter
import com.danylom73.rescuehelper.domain.nearby.NearbyEvent
import com.danylom73.rescuehelper.domain.nearby.NearbyHost
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback

class EndpointDiscoveryHandler(
    private val emit: NearbyEventEmitter
) : EndpointDiscoveryCallback() {

    override fun onEndpointFound(
        endpointId: String,
        info: DiscoveredEndpointInfo
    ) {
        emit(
            NearbyEvent.HostFound(
                NearbyHost(
                    endpointId = endpointId,
                    name = info.endpointName
                )
            )
        )
    }

    override fun onEndpointLost(endpointId: String) {
        emit(NearbyEvent.HostLost(endpointId))
    }
}

