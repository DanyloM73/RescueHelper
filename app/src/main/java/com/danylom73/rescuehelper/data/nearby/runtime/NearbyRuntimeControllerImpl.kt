package com.danylom73.rescuehelper.data.nearby.runtime

import com.danylom73.rescuehelper.core.role.AppRole
import com.danylom73.rescuehelper.core.role.RoleProvider
import com.danylom73.rescuehelper.di.ApplicationScope
import com.danylom73.rescuehelper.domain.alert.AlertController
import com.danylom73.rescuehelper.domain.flashlight.FlashlightController
import com.danylom73.rescuehelper.domain.nearby.NearbyCommand
import com.danylom73.rescuehelper.domain.nearby.NearbyEvent
import com.danylom73.rescuehelper.domain.nearby.NearbyRepository
import com.danylom73.rescuehelper.domain.nearby.NearbyRuntimeController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NearbyRuntimeControllerImpl @Inject constructor(
    private val repository: NearbyRepository,
    private val roleProvider: RoleProvider,
    private val flashlightController: FlashlightController,
    private val alertController: AlertController,
    @param:ApplicationScope private val appScope: CoroutineScope
) : NearbyRuntimeController {

    private val _connectionState = MutableStateFlow(NearbyRuntimeState())
    override val connectionState = _connectionState.asStateFlow()

    private var observeJob: Job? = null

    override val events: Flow<NearbyEvent> = repository.observeEvents()

    override fun startForCurrentRole() {
        observeEventsIfNeeded()

        when (roleProvider.role) {
            AppRole.USER -> repository.startAdvertising()
            AppRole.RESPONDER -> repository.startDiscovery()
        }

        _connectionState.update {
            it.copy(
                isRunning = true,
                isAdvertising = roleProvider.role == AppRole.USER,
                isDiscovering = roleProvider.role == AppRole.RESPONDER
            )
        }
    }

    override fun stop() {
        repository.stopAdvertising()
        repository.stopDiscovery()
        repository.disconnect()

        flashlightController.stopBlinking()
        flashlightController.setEnabled(false)
        alertController.stopAlert()

        _connectionState.update { NearbyRuntimeState() }
    }

    override fun connectToHost(endpointId: String) {
        if (roleProvider.role != AppRole.RESPONDER) return

        observeEventsIfNeeded()
        repository.connectToHost(endpointId)
    }

    override fun sendCommand(command: NearbyCommand) {
        repository.sendCommand(command)
    }

    private fun observeEventsIfNeeded() {
        if (observeJob?.isActive == true) return

        observeJob = appScope.launch {
            repository.observeEvents().collect { event ->
                when (event) {
                    is NearbyEvent.Connected -> {
                        _connectionState.update {
                            it.copy(
                                connectedEndpointId = event.endpointId,
                                isDiscovering = false
                            )
                        }
                    }

                    NearbyEvent.Disconnected -> {
                        _connectionState.update {
                            it.copy(connectedEndpointId = null)
                        }
                    }

                    is NearbyEvent.CommandReceived -> {
                        handleCommand(event.command)
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun handleCommand(command: NearbyCommand) {
        when (command) {
            NearbyCommand.START_FLASHLIGHT_BLINKING -> {
                flashlightController.startBlinking()
            }

            NearbyCommand.STOP_FLASHLIGHT_BLINKING -> {
                flashlightController.stopBlinking()
                flashlightController.setEnabled(false)
            }

            NearbyCommand.START_ALERT -> {
                alertController.startAlert()
            }

            NearbyCommand.STOP_ALERT -> {
                alertController.stopAlert()
            }

            else -> Unit
        }
    }
}