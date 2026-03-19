package com.danylom73.rescuehelper.domain.requirement

import kotlinx.coroutines.flow.Flow

interface RequirementRepository {
    fun check(): List<Requirement>
    fun observeRequirements(): Flow<List<Requirement>>
}