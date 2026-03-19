package com.danylom73.rescuehelper.domain.requirement

data class Requirement(
    val type: RequirementType,
    val isGranted: Boolean
)