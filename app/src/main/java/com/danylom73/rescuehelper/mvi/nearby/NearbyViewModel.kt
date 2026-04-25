package com.danylom73.rescuehelper.mvi.nearby

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danylom73.rescuehelper.R
import com.danylom73.rescuehelper.domain.alert.AlertController
import com.danylom73.rescuehelper.domain.flashlight.FlashlightController
import com.danylom73.rescuehelper.domain.nearby.NearbyEvent
import com.danylom73.rescuehelper.domain.nearby.NearbyRuntimeController
import com.danylom73.rescuehelper.domain.resource.ResourceManager
import com.danylom73.rescuehelper.presentation.components.base.BaseSnackbarIcon
import com.danylom73.rescuehelper.presentation.screen.config.NearbyScreenUiConfig
import com.danylom73.rescuehelper.service.NearbyConnectionService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    private val runtimeController: NearbyRuntimeController,
    private val flashlightController: FlashlightController,
    private val alertController: AlertController,
    private val resourceManager: ResourceManager,
    nearbyScreenUiConfig: NearbyScreenUiConfig,
    @param:ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(NearbyState())
    val state: StateFlow<NearbyState> = _state.asStateFlow()

    private val _sideEffect = Channel<NearbySideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    val uiConfig = nearbyScreenUiConfig

    init {
        observeRuntimeState()
        observeRuntimeEvents()
        observeLocalDeviceState()
    }

    private fun observeRuntimeState() {
        viewModelScope.launch {
            runtimeController.connectionState.collect { runtimeState ->
                updateState {
                    copy(
                        isAdvertising = runtimeState.isAdvertising,
                        isDiscovering = runtimeState.isDiscovering,
                        connectedEndpointId = runtimeState.connectedEndpointId
                    )
                }
            }
        }
    }

    private fun observeLocalDeviceState() {
        viewModelScope.launch {
            flashlightController.isFlashlightEnabled.collect { enabled ->
                updateState {
                    copy(isLocalFlashlightEnabled = enabled)
                }
            }
        }

        viewModelScope.launch {
            alertController.isPlayingFlow.collect { enabled ->
                updateState {
                    copy(isLocalAlertEnabled = enabled)
                }
            }
        }
    }

    fun handleIntent(intent: NearbyIntent) {
        when (intent) {
            is NearbyIntent.StartConnecting -> {
                NearbyConnectionService.start(context)
            }

            is NearbyIntent.StopConnecting -> {
                NearbyConnectionService.stop(context)
            }

            is NearbyIntent.SendCommand -> {
                runtimeController.sendCommand(intent.command)
            }

            is NearbyIntent.ConnectToHost -> {
                runtimeController.connectToHost(intent.endpointId)
            }

            is NearbyIntent.Disconnect -> {
                runtimeController.stop()
                NearbyConnectionService.stop(context)
            }

            else -> {}
        }
    }

    private fun observeRuntimeEvents() {
        viewModelScope.launch {
            runtimeController.events.collect { event ->
                when (event) {
                    is NearbyEvent.HostFound -> {
                        updateState {
                            if (discoveredHosts.any { it.endpointId == event.host.endpointId }) this
                            else copy(discoveredHosts = discoveredHosts + event.host)
                        }
                    }

                    is NearbyEvent.HostLost -> {
                        updateState {
                            copy(
                                discoveredHosts = discoveredHosts.filterNot {
                                    it.endpointId == event.endpointId
                                }
                            )
                        }
                    }

                    is NearbyEvent.Connected -> {
                        updateState {
                            copy(
                                connectedEndpointId = event.endpointId,
                                isDiscovering = false,
                                isAdvertising = false,
                                discoveredHosts = emptyList()
                            )
                        }

                        postSideEffect(
                            NearbySideEffect.ShowMessage(
                                resourceManager.getString(R.string.nearby_connected_message),
                                BaseSnackbarIcon.CONNECTED
                            )
                        )
                    }

                    NearbyEvent.Disconnected -> {
                        updateState {
                            copy(connectedEndpointId = null)
                        }

                        postSideEffect(
                            NearbySideEffect.ShowMessage(
                                resourceManager.getString(R.string.nearby_disconnected_message),
                                BaseSnackbarIcon.DISCONNECTED
                            )
                        )
                    }

                    is NearbyEvent.Error -> {
                        updateState { copy(error = event.error) }
                        postSideEffect(NearbySideEffect.ShowMessage(event.error))
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun updateState(reducer: NearbyState.() -> NearbyState) {
        _state.update { it.reducer() }
    }

    private fun postSideEffect(effect: NearbySideEffect) {
        viewModelScope.launch {
            _sideEffect.send(effect)
        }
    }
}
