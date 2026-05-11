package com.danylom73.rescuehelper.domain.requirement

interface RequirementMonitor {
    fun start(onRequirementLost: (RequirementType) -> Unit)
    fun stop()
}