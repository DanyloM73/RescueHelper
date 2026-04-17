package com.danylom73.rescuehelper.mvi.nearby

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danylom73.rescuehelper.core.role.AppRole
import com.danylom73.rescuehelper.core.role.RoleProvider
import com.danylom73.rescuehelper.domain.alert.AlertController
import com.danylom73.rescuehelper.domain.flashlight.FlashlightController
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
    private val flashlightController: FlashlightController,
    private val alertController: AlertController,
    nearbyScreenUiConfig: NearbyScreenUiConfig
) : ViewModel() {

    private val _state = MutableStateFlow(NearbyState())
    val state: StateFlow<NearbyState> = _state.asStateFlow()

    private val _sideEffect = Channel<NearbySideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    val uiConfig = nearbyScreenUiConfig

    val isFlashlightEnabled: StateFlow<Boolean> = flashlightController.isFlashlightEnabled
    val isAlertPlaying: StateFlow<Boolean> = alertController.isPlayingFlow

    init {
        observeRepository()
    }

    fun handleIntent(intent: NearbyIntent) {
        when (intent) {
            is NearbyIntent.StartConnecting -> {
                when (roleProvider.role) {
                    AppRole.RESPONDER -> {
                        //sendSideEffect(NearbySideEffect.ShowToast("Discovery started"))
                        repository.startDiscovery()
                        updateState { copy(isDiscovering = true) }
                    }

                    AppRole.USER ->  {
                        //sendSideEffect(NearbySideEffect.ShowToast("Advertising started"))
                        repository.startAdvertising()
                        updateState { copy(isAdvertising = true) }
                    }
                }
            }

            is NearbyIntent.StopConnecting -> {
                when (roleProvider.role) {
                    AppRole.RESPONDER -> {
                        //sendSideEffect(NearbySideEffect.ShowToast("Discovery stopped"))
                        repository.stopDiscovery()
                        updateState { copy(isDiscovering = false) }
                    }

                    AppRole.USER ->  {
                        //sendSideEffect(NearbySideEffect.ShowToast("Advertising stopped"))
                        repository.stopAdvertising()
                        updateState { copy(isAdvertising = false) }
                    }
                }
            }

            is NearbyIntent.SendCommand -> repository.sendCommand(intent.command)

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

            is NearbyIntent.SendCurrentAlertState -> {
                if (roleProvider.role == AppRole.USER) {
                    repository.sendCommand(
                        if (intent.enabled) {
                            NearbyCommand.ALERT_STATE_ON
                        } else {
                            NearbyCommand.ALERT_STATE_OFF
                        }
                    )
                }
            }

            is NearbyIntent.ConnectToHost -> {
                //sendSideEffect(NearbySideEffect.ShowToast("Connecting to host"))
                repository.connectToHost(intent.endpointId)
            }

            is NearbyIntent.Disconnect -> {
                //sendSideEffect(NearbySideEffect.ShowToast("Disconnecting"))
                repository.disconnect()
            }
        }
    }

    private fun observeRepository() {
        viewModelScope.launch {
            repository.observeEvents().collect { event ->
                when (event) {
                    is NearbyEvent.HostFound ->
                        updateState {
                            if (discoveredHosts.any { it.endpointId == event.host.endpointId }) this
                            else copy(discoveredHosts = discoveredHosts + event.host)
                        }

                    is NearbyEvent.HostLost ->
                        updateState {
                            copy(
                                discoveredHosts = discoveredHosts.filterNot {
                                    it.endpointId == event.endpointId
                                }
                            )
                        }

                    is NearbyEvent.Connected ->
                        updateState {
                            copy(
                                isDiscovering = false,
                                connectedEndpointId = event.endpointId,
                                discoveredHosts = emptyList()
                            )
                        }

                    is NearbyEvent.Disconnected ->
                        updateState {
                            copy(
                                connectedEndpointId = null
                            )
                        }

                    is NearbyEvent.CommandReceived -> {
                        when (event.command) {
                            NearbyCommand.START_FLASHLIGHT_BLINKING -> flashlightController.startBlinking()
                            NearbyCommand.STOP_FLASHLIGHT_BLINKING -> {
                                flashlightController.stopBlinking()
                                flashlightController.setEnabled(false)
                            }

                            NearbyCommand.START_ALERT -> alertController.startAlert()
                            NearbyCommand.STOP_ALERT -> alertController.stopAlert()

                            NearbyCommand.FLASHLIGHT_STATE_ON ->
                                updateState { copy(remoteFlashlightEnabled = true) }
                            NearbyCommand.FLASHLIGHT_STATE_OFF ->
                                updateState { copy(remoteFlashlightEnabled = false) }

                            NearbyCommand.ALERT_STATE_ON ->
                                updateState { copy(remoteAlertEnabled = true) }
                            NearbyCommand.ALERT_STATE_OFF ->
                                updateState { copy(remoteAlertEnabled = false) }
                        }
                    }

                    is NearbyEvent.Error -> {
                        updateState { copy(error = event.error) }
                        sendSideEffect(NearbySideEffect.ShowToast(event.error))
                    }
                }
            }
        }
    }

    private fun updateState(reducer: NearbyState.() -> NearbyState) {
        _state.update { it.reducer() }
    }

    private fun sendSideEffect(effect: NearbySideEffect) {
        viewModelScope.launch {
            _sideEffect.send(effect)
        }
    }
}
