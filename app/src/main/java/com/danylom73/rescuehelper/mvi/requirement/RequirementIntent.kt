package com.danylom73.rescuehelper.mvi.requirement

sealed interface RequirementIntent {
    data object OnRequirementRefresh : RequirementIntent
}