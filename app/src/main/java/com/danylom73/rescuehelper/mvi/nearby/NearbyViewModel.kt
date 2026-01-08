package com.danylom73.rescuehelper.mvi.nearby

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danylom73.rescuehelper.domain.nearby.NearbyEvent
import com.danylom73.rescuehelper.domain.nearby.NearbyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NearbyViewModel @Inject constructor(
    private val repository: NearbyRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NearbyState())
    val state: StateFlow<NearbyState> = _state.asStateFlow()

    private val _sideEffect = Channel<NearbySideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        observeRepository()
    }

    fun process(intent: NearbyIntent) {
        when (intent) {
            is NearbyIntent.StartAdvertising -> {
                sendSideEffect(NearbySideEffect.ShowToast("Advertising started"))
                repository.startAdvertising()
                reduce { copy(isAdvertising = true) }
            }

            is NearbyIntent.StartDiscovery -> {
                sendSideEffect(NearbySideEffect.ShowToast("Discovery started"))
                repository.startDiscovery()
                reduce { copy(isDiscovering = true) }
            }

            is NearbyIntent.SendMessage -> {
                repository.sendMessage(intent.message)
            }

            is NearbyIntent.ConnectToHost -> {
                sendSideEffect(NearbySideEffect.ShowToast("Connecting to host"))
                repository.connectToHost(intent.endpointId)
            }
        }
    }

    private fun observeRepository() {
        viewModelScope.launch {
            repository.observeEvents().collect { event ->
                when (event) {
                    is NearbyEvent.HostFound ->
                        reduce {
                            if (discoveredHosts.any { it.endpointId == event.host.endpointId }) this
                            else copy(discoveredHosts = discoveredHosts + event.host)
                        }

                    is NearbyEvent.HostLost ->
                        reduce {
                            copy(
                                discoveredHosts = discoveredHosts.filterNot {
                                    it.endpointId == event.endpointId
                                }
                            )
                        }

                    is NearbyEvent.Connected ->
                        reduce {
                            copy(
                                connectedEndpointId = event.endpointId,
                                discoveredHosts = emptyList()
                            )
                        }

                    is NearbyEvent.MessageReceived ->
                        reduce { copy(messages = messages + event.message) }

                    is NearbyEvent.Error ->
                        reduce { copy(error = event.error) }
                }
            }
        }
    }

    private fun reduce(reducer: NearbyState.() -> NearbyState) {
        _state.update { it.reducer() }
    }

    private fun sendSideEffect(effect: NearbySideEffect) {
        viewModelScope.launch {
            _sideEffect.send(effect)
        }
    }
}
