package com.danylom73.rescuehelper.data.nearby.handler

import android.os.Build
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback
import com.google.android.gms.nearby.connection.ConnectionsClient
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback

class EndpointDiscoveryHandler(
    private val client: ConnectionsClient,
    private val connectionCallback: ConnectionLifecycleCallback
) : EndpointDiscoveryCallback() {

    override fun onEndpointFound(
        endpointId: String,
        info: DiscoveredEndpointInfo
    ) {
        client.requestConnection(
            Build.MODEL,
            endpointId,
            connectionCallback
        )
    }

    override fun onEndpointLost(endpointId: String) = Unit
}
