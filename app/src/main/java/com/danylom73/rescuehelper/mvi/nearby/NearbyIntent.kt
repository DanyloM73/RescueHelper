package com.danylom73.rescuehelper.mvi.nearby

import com.danylom73.rescuehelper.domain.nearby.NearbyCommand

sealed interface NearbyIntent {
    data object StartConnecting : NearbyIntent
    data object StopConnecting : NearbyIntent
    data class ConnectToHost(val endpointId: String) : NearbyIntent
    data object Disconnect : NearbyIntent
    data class SendCommand(val command: NearbyCommand) : NearbyIntent
    data class SendCurrentFlashlightState(val enabled: Boolean) : NearbyIntent
}
