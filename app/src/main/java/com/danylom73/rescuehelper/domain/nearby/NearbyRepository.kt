package com.danylom73.rescuehelper.domain.nearby

import kotlinx.coroutines.flow.Flow

interface NearbyRepository {
    fun startAdvertising()
    fun startDiscovery()
    fun stopAdvertising()
    fun stopDiscovery()
    fun connectToHost(endpointId: String)
    fun disconnect()
    fun sendMessage(message: String)
    fun observeEvents(): Flow<NearbyEvent>
}
