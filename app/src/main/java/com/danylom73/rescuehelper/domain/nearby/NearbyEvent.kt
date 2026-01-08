package com.danylom73.rescuehelper.domain.nearby

sealed interface NearbyEvent {
    data class Connected(val endpointId: String) : NearbyEvent
    data class MessageReceived(val message: String) : NearbyEvent
    data class Error(val error: String) : NearbyEvent
}
