package com.danylom73.rescuehelper.data.requirement

import com.danylom73.rescuehelper.di.ApplicationScope
import com.danylom73.rescuehelper.domain.requirement.RequirementMonitor
import com.danylom73.rescuehelper.domain.requirement.RequirementRepository
import com.danylom73.rescuehelper.domain.requirement.RequirementType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequirementMonitorImpl @Inject constructor(
    private val requirementRepository: RequirementRepository,
    @param:ApplicationScope private val appScope: CoroutineScope
) : RequirementMonitor {

    private var job: Job? = null

    override fun start(onRequirementLost: (RequirementType) -> Unit) {
        if (job?.isActive == true) return

        job = appScope.launch {
            requirementRepository.observeRequirements()
                .distinctUntilChanged()
                .collect { requirements ->
                    val failedRequirement = requirements.firstOrNull { requirement ->
                        !requirement.isGranted &&
                                requirement.type.isCriticalForNearby()
                    }

                    if (failedRequirement != null) {
                        onRequirementLost(failedRequirement.type)
                    }
                }
        }
    }

    override fun stop() {
        job?.cancel()
        job = null
    }

    private fun RequirementType.isCriticalForNearby(): Boolean {
        return when (this) {
            RequirementType.NearbyPermission,
            RequirementType.LocationPermission,
            RequirementType.BluetoothEnabled,
            RequirementType.LocationEnabled -> true

            RequirementType.WifiEnabled,
            RequirementType.NotificationPermission -> false
        }
    }
}