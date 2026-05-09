package com.danylom73.rescuehelper.mvi.requirement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danylom73.rescuehelper.domain.prefs.PrefsManager
import com.danylom73.rescuehelper.domain.requirement.RequirementRepository
import com.danylom73.rescuehelper.util.PrefsKeyIds
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
class RequirementViewModel @Inject constructor(
    private val repository: RequirementRepository,
    private val prefsManager: PrefsManager
) : ViewModel() {

    private val _state = MutableStateFlow(RequirementState())
    val state: StateFlow<RequirementState> = _state.asStateFlow()

    private val _sideEffect = Channel<RequirementSideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        observeRequirements()
        getAutoContinueValue()
    }

    fun handleIntent(intent: RequirementIntent) {
        when (intent) {
            is RequirementIntent.OnRequirementRefresh -> refreshNow()

            is RequirementIntent.OnContinue -> {
                viewModelScope.launch {
                    prefsManager.saveBoolean(PrefsKeyIds.AUTO_CONTINUE, intent.autoContinueValue)
                    postSideEffect(RequirementSideEffect.GoToMain)
                }
            }
        }
    }

    private fun observeRequirements() {
        viewModelScope.launch {
            repository.observeRequirements().collect { requirements ->
                updateState {
                    copy(requirements = requirements.groupBy { it.type.category })
                }
            }
        }
    }

    private fun getAutoContinueValue() {
        viewModelScope.launch {
            val checked = prefsManager.getBoolean(PrefsKeyIds.AUTO_CONTINUE)
            updateState { copy(autoContinue = checked) }
        }
    }

    private fun refreshNow() {
        val requirements = repository.check().groupBy { it.type.category }
        updateState { copy(requirements = requirements) }
    }

    private fun updateState(reducer: RequirementState.() -> RequirementState) {
        _state.update { it.reducer() }
    }

    private fun postSideEffect(effect: RequirementSideEffect) {
        viewModelScope.launch {
            _sideEffect.send(effect)
        }
    }
}