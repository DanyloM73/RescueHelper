package com.danylom73.rescuehelper.data.nearby.handler

import com.danylom73.rescuehelper.data.nearby.NearbyEventEmitter
import com.danylom73.rescuehelper.domain.nearby.NearbyEvent
import com.google.android.gms.nearby.connection.ConnectionInfo
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback
import com.google.android.gms.nearby.connection.ConnectionResolution
import com.google.android.gms.nearby.connection.ConnectionsClient
import com.google.android.gms.nearby.connection.PayloadCallback

class ConnectionLifecycleHandler(
    private val client: ConnectionsClient,
    private val payloadCallback: PayloadCallback,
    private val emit: NearbyEventEmitter
) : ConnectionLifecycleCallback() {

    override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
        client.acceptConnection(endpointId, payloadCallback)
    }

    override fun onConnectionResult(
        endpointId: String,
        result: ConnectionResolution
    ) {
        if (result.status.isSuccess) {
            client.stopDiscovery()
            emit(NearbyEvent.Connected(endpointId))
        } else {
            emit(NearbyEvent.Error("Connection failed"))
        }
    }

    override fun onDisconnected(endpointId: String) {
        emit(NearbyEvent.Disconnected)
    }
}
