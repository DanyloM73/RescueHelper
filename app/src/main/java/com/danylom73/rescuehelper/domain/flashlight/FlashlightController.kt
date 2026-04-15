package com.danylom73.rescuehelper.domain.flashlight

import kotlinx.coroutines.flow.StateFlow

interface FlashlightController {
    val isFlashlightEnabled: StateFlow<Boolean>
    fun setEnabled(enabled: Boolean)
    fun startBlinking(intervalMillis: Long = 1000L)
    fun stopBlinking()
}