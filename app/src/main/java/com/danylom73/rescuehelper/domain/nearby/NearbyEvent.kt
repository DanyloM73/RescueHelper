package com.danylom73.rescuehelper.domain.nearby

sealed interface NearbyEvent {
    data class HostFound(val host: NearbyHost) : NearbyEvent
    data class HostLost(val endpointId: String) : NearbyEvent
    data class Connected(val endpointId: String) : NearbyEvent
    data object Disconnected : NearbyEvent
    data class CommandReceived(val command: NearbyCommand) : NearbyEvent
    data class Error(val error: String) : NearbyEvent
}
