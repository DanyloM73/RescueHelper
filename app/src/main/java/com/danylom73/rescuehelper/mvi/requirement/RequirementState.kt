package com.danylom73.rescuehelper.mvi.requirement

import com.danylom73.rescuehelper.domain.requirement.Requirement
import com.danylom73.rescuehelper.domain.requirement.RequirementCategory

data class RequirementState(
    val requirements: Map<RequirementCategory, List<Requirement>> = emptyMap()
)