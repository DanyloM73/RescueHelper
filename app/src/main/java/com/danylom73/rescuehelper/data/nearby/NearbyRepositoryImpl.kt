package com.danylom73.rescuehelper.data.nearby

import android.content.Context
import android.os.Build
import com.danylom73.rescuehelper.data.nearby.handler.ConnectionLifecycleHandler
import com.danylom73.rescuehelper.data.nearby.handler.EndpointDiscoveryHandler
import com.danylom73.rescuehelper.data.nearby.handler.PayloadHandler
import com.danylom73.rescuehelper.domain.nearby.NearbyCommand
import com.danylom73.rescuehelper.domain.nearby.NearbyEvent
import com.danylom73.rescuehelper.domain.nearby.NearbyRepository
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.AdvertisingOptions
import com.google.android.gms.nearby.connection.DiscoveryOptions
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.Strategy
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

typealias NearbyEventEmitter = (NearbyEvent) -> Unit

@Singleton
class NearbyRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : NearbyRepository {

    private val client = Nearby.getConnectionsClient(context)

    private val events = MutableSharedFlow<NearbyEvent>(
        extraBufferCapacity = 64
    )

    private var currentEndpoint: String? = null

    private val emit: NearbyEventEmitter = { event ->
        when (event) {
            is NearbyEvent.Connected -> currentEndpoint = event.endpointId
            NearbyEvent.Disconnected -> currentEndpoint = null
            else -> Unit
        }

        events.tryEmit(event)
    }

    private val payloadHandler = PayloadHandler(emit)

    private val connectionHandler =
        ConnectionLifecycleHandler(client, payloadHandler, emit)

    private val discoveryHandler = EndpointDiscoveryHandler(emit)

    override fun startAdvertising() {
        client.startAdvertising(
            Build.MODEL,
            SERVICE_ID,
            connectionHandler,
            AdvertisingOptions.Builder()
                .setStrategy(Strategy.P2P_POINT_TO_POINT)
                .build()
        ).addOnFailureListener {
            emit(NearbyEvent.Error(it.message ?: "Advertising start failed"))
        }
    }

    override fun startDiscovery() {
        client.startDiscovery(
            SERVICE_ID,
            discoveryHandler,
            DiscoveryOptions.Builder()
                .setStrategy(Strategy.P2P_POINT_TO_POINT)
                .build()
        ).addOnFailureListener {
            emit(NearbyEvent.Error(it.message ?: "Discovery start failed"))
        }
    }

    override fun stopAdvertising() {
        client.stopAdvertising()
    }

    override fun stopDiscovery() {
        client.stopDiscovery()
    }

    override fun connectToHost(endpointId: String) {
        client.requestConnection(
            Build.DEVICE,
            endpointId,
            connectionHandler
        ).addOnFailureListener {
            emit(NearbyEvent.Error(it.message ?: "Connection request failed"))
        }
    }

    override fun disconnect() {
        currentEndpoint?.let { endpoint ->
            client.disconnectFromEndpoint(endpoint)
        }

        client.stopAllEndpoints()
        currentEndpoint = null
        emit(NearbyEvent.Disconnected)
    }

    override fun sendCommand(command: NearbyCommand) {
        val endpoint = currentEndpoint

        if (endpoint == null) {
            emit(NearbyEvent.Error("No connected endpoint"))
            return
        }

        client.sendPayload(
            endpoint,
            Payload.fromBytes(command.name.toByteArray())
        ).addOnFailureListener {
            emit(NearbyEvent.Error(it.message ?: "Command send failed"))
        }
    }

    override fun observeEvents(): Flow<NearbyEvent> = events

    companion object {
        private const val SERVICE_ID = "nearby_service"
    }
}
