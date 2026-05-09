package com.danylom73.rescuehelper.mvi.requirement

sealed interface RequirementSideEffect {
    data object GoToMain : RequirementSideEffect
}