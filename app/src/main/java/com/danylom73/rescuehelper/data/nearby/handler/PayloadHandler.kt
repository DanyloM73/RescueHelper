package com.danylom73.rescuehelper.data.nearby.handler

import com.danylom73.rescuehelper.data.nearby.NearbyEventEmitter
import com.danylom73.rescuehelper.domain.nearby.NearbyCommand
import com.danylom73.rescuehelper.domain.nearby.NearbyEvent
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadCallback
import com.google.android.gms.nearby.connection.PayloadTransferUpdate

class PayloadHandler(
    private val emit: NearbyEventEmitter
) : PayloadCallback() {

    override fun onPayloadReceived(endpointId: String, payload: Payload) {
        payload.asBytes()?.let { bytes ->
            val command = NearbyCommand.fromName(String(bytes))

            command?.let { emit(NearbyEvent.CommandReceived(it)) }
        }
    }

    override fun onPayloadTransferUpdate(
        endpointId: String,
        update: PayloadTransferUpdate
    ) = Unit
}
