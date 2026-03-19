package com.danylom73.rescuehelper.domain.requirement

interface RequirementRepository {
    fun check(): List<Requirement>
}