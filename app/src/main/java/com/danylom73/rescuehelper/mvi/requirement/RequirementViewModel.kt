package com.danylom73.rescuehelper.mvi.requirement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danylom73.rescuehelper.domain.requirement.RequirementRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequirementViewModel @Inject constructor(
    private val repository: RequirementRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RequirementState())
    val state: StateFlow<RequirementState> = _state.asStateFlow()

    init {
        observeRequirements()
    }

    fun process(intent: RequirementIntent) {
        when (intent) {
            RequirementIntent.OnRequirementRefresh -> refreshNow()
        }
    }

    private fun observeRequirements() {
        viewModelScope.launch {
            repository.observeRequirements().collect { requirements ->
                reduce {
                    copy(requirements = requirements.groupBy { it.type.category })
                }
            }
        }
    }

    private fun refreshNow() {
        val requirements = repository.check().groupBy { it.type.category }
        reduce { copy(requirements = requirements) }
    }

    private fun reduce(reducer: RequirementState.() -> RequirementState) {
        _state.update { it.reducer() }
    }
}