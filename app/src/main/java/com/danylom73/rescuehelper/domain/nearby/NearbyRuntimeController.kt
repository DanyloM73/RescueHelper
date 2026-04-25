package com.danylom73.rescuehelper.domain.nearby

import com.danylom73.rescuehelper.data.nearby.runtime.NearbyRuntimeState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface NearbyRuntimeController {
    fun startForCurrentRole()
    fun stop()
    fun connectToHost(endpointId: String)
    fun sendCommand(command: NearbyCommand)
    val connectionState: StateFlow<NearbyRuntimeState>
    val events: Flow<NearbyEvent>
}