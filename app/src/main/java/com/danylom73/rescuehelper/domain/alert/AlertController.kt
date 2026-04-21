package com.danylom73.rescuehelper.domain.alert

import kotlinx.coroutines.flow.StateFlow

interface AlertController {
    val isPlayingFlow: StateFlow<Boolean>
    fun isPlaying(): Boolean
    fun startAlert(
        soundVolume: Float = 1f,
        withVibration: Boolean = true
    )
    fun stopAlert()
    fun setSoundVolume(volume: Float)
    fun isAlertRunning(): Boolean
}