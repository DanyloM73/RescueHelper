package com.danylom73.rescuehelper.data.nearby

import android.content.Context
import android.os.Build
import com.danylom73.rescuehelper.data.nearby.handler.ConnectionLifecycleHandler
import com.danylom73.rescuehelper.data.nearby.handler.EndpointDiscoveryHandler
import com.danylom73.rescuehelper.data.nearby.handler.PayloadHandler
import com.danylom73.rescuehelper.domain.nearby.NearbyEvent
import com.danylom73.rescuehelper.domain.nearby.NearbyRepository
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.AdvertisingOptions
import com.google.android.gms.nearby.connection.DiscoveryOptions
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.Strategy
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

typealias NearbyEventEmitter = (NearbyEvent) -> Unit

@Singleton
class NearbyRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : NearbyRepository {

    private val client = Nearby.getConnectionsClient(context)
    private val events = MutableSharedFlow<NearbyEvent>()

    private var currentEndpoint: String? = null

    private val emit: NearbyEventEmitter = { event ->
        CoroutineScope(Dispatchers.IO).launch {
            events.emit(event)
        }
    }

    private val payloadHandler = PayloadHandler(emit)

    private val connectionHandler =
        ConnectionLifecycleHandler(client, payloadHandler, emit)

    private val discoveryHandler = EndpointDiscoveryHandler(emit)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            events.collect { event ->
                when (event) {
                    is NearbyEvent.Connected -> {
                        currentEndpoint = event.endpointId
                    }

                    NearbyEvent.Disconnected -> {
                        currentEndpoint = null
                    }

                    else -> Unit
                }
            }
        }
    }

    override fun startAdvertising() {
        client.startAdvertising(
            Build.MODEL,
            SERVICE_ID,
            connectionHandler,
            AdvertisingOptions.Builder()
                .setStrategy(Strategy.P2P_POINT_TO_POINT)
                .build()
        )
    }

    override fun startDiscovery() {
        client.startDiscovery(
            SERVICE_ID,
            discoveryHandler,
            DiscoveryOptions.Builder()
                .setStrategy(Strategy.P2P_POINT_TO_POINT)
                .build()
        )
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
        )
    }

    override fun disconnect() {
        currentEndpoint?.let {
            client.disconnectFromEndpoint(it)
        }

        client.stopAllEndpoints()
        currentEndpoint = null
    }


    override fun sendMessage(message: String) {
        val endpoint = requireNotNull(currentEndpoint)
        client.sendPayload(
            endpoint,
            Payload.fromBytes(message.toByteArray())
        )
    }

    override fun observeEvents(): Flow<NearbyEvent> = events

    companion object {
        private const val SERVICE_ID = "nearby_service"
    }
}
