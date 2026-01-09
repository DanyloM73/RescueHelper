package com.danylom73.rescuehelper.ui

import com.danylom73.rescuehelper.presentation.screen.config.NearbyScreenUiConfig
import javax.inject.Inject

class UserNearbyScreenUiConfig @Inject constructor() : NearbyScreenUiConfig {
    override val title = "Find help nearby"
    override val showHosts = false
    override val primaryButtonText = "Turn on host"
}
