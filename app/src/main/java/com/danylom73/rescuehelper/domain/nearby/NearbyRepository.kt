package com.danylom73.rescuehelper.domain.nearby

import kotlinx.coroutines.flow.Flow

interface NearbyRepository {
    fun startAdvertising()
    fun startDiscovery()
    fun sendMessage(message: String)
    fun observeEvents(): Flow<NearbyEvent>
}
