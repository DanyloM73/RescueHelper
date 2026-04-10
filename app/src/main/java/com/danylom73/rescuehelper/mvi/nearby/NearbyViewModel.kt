package com.danylom73.rescuehelper.mvi.nearby

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danylom73.rescuehelper.core.role.AppRole
import com.danylom73.rescuehelper.core.role.RoleProvider
import com.danylom73.rescuehelper.domain.nearby.NearbyCommand
import com.danylom73.rescuehelper.domain.nearby.NearbyEvent
import com.danylom73.rescuehelper.domain.nearby.NearbyRepository
import com.danylom73.rescuehelper.presentation.screen.config.NearbyScreenUiConfig
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
    private val repository: NearbyRepository,
    private val roleProvider: RoleProvider,
    nearbyScreenUiConfig: NearbyScreenUiConfig
) : ViewModel() {

    private val _state = MutableStateFlow(NearbyState())
    val state: StateFlow<NearbyState> = _state.asStateFlow()

    private val _sideEffect = Channel<NearbySideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    val uiConfig = nearbyScreenUiConfig

    init {
        observeRepository()
    }

    fun process(intent: NearbyIntent) {
        when (intent) {
            is NearbyIntent.StartConnecting -> {
                when (roleProvider.role) {
                    AppRole.RESPONDER -> {
                        sendSideEffect(NearbySideEffect.ShowToast("Discovery started"))
                        repository.startDiscovery()
                        reduce { copy(isDiscovering = true) }
                    }

                    AppRole.USER ->  {
                        sendSideEffect(NearbySideEffect.ShowToast("Advertising started"))
                        repository.startAdvertising()
                        reduce { copy(isAdvertising = true) }
                    }
                }
            }

            is NearbyIntent.StopConnecting -> {
                when (roleProvider.role) {
                    AppRole.RESPONDER -> {
                        sendSideEffect(NearbySideEffect.ShowToast("Discovery stopped"))
                        repository.stopDiscovery()
                        reduce { copy(isDiscovering = false) }
                    }

                    AppRole.USER ->  {
                        sendSideEffect(NearbySideEffect.ShowToast("Advertising stopped"))
                        repository.stopAdvertising()
                        reduce { copy(isAdvertising = false) }
                    }
                }
            }

            is NearbyIntent.SendCommand ->
                repository.sendCommand(intent.command)

            is NearbyIntent.SendCurrentFlashlightState -> {
                if (roleProvider.role == AppRole.USER) {
                    repository.sendCommand(
                        if (intent.enabled) {
                            NearbyCommand.FLASHLIGHT_STATE_ON
                        } else {
                            NearbyCommand.FLASHLIGHT_STATE_OFF
                        }
                    )
                }
            }

            is NearbyIntent.ConnectToHost -> {
                sendSideEffect(NearbySideEffect.ShowToast("Connecting to host"))
                repository.connectToHost(intent.endpointId)
            }

            is NearbyIntent.Disconnect -> {
                sendSideEffect(NearbySideEffect.ShowToast("Disconnecting"))
                repository.disconnect()
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
                                isDiscovering = false,
                                connectedEndpointId = event.endpointId,
                                discoveredHosts = emptyList()
                            )
                        }

                    is NearbyEvent.Disconnected ->
                        reduce {
                            copy(
                                connectedEndpointId = null
                            )
                        }

                    is NearbyEvent.CommandReceived -> {
                        when (event.command) {
                            NearbyCommand.TURN_ON_FLASHLIGHT ->
                                sendSideEffect(NearbySideEffect.SetFlashlight(true))
                            NearbyCommand.TURN_OFF_FLASHLIGHT ->
                                sendSideEffect(NearbySideEffect.SetFlashlight(false))
                            NearbyCommand.FLASHLIGHT_STATE_ON ->
                                reduce { copy(remoteFlashlightEnabled = true) }
                            NearbyCommand.FLASHLIGHT_STATE_OFF ->
                                reduce { copy(remoteFlashlightEnabled = false) }
                        }
                    }

                    is NearbyEvent.Error -> {
                        reduce { copy(error = event.error) }
                        sendSideEffect(NearbySideEffect.ShowToast(event.error))
                    }
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
