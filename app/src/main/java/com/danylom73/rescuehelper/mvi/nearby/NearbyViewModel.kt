package com.danylom73.rescuehelper.mvi.nearby

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danylom73.rescuehelper.domain.nearby.NearbyEvent
import com.danylom73.rescuehelper.domain.nearby.NearbyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NearbyViewModel @Inject constructor(
    private val repository: NearbyRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NearbyState())
    val state: StateFlow<NearbyState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<NearbySideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        observeRepository()
    }

    fun process(intent: NearbyIntent) {
        when (intent) {
            NearbyIntent.StartAdvertising -> {
                repository.startAdvertising()
                reduce { copy(isAdvertising = true) }
            }

            NearbyIntent.StartDiscovery -> {
                repository.startDiscovery()
                reduce { copy(isDiscovering = true) }
            }

            is NearbyIntent.SendMessage -> {
                repository.sendMessage(intent.message)
            }
        }
    }

    private fun observeRepository() {
        viewModelScope.launch {
            repository.observeEvents().collect { event ->
                when (event) {
                    is NearbyEvent.Connected ->
                        reduce { copy(connectedEndpointId = event.endpointId) }

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
}
