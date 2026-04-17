package com.danylom73.rescuehelper.data.alert

import android.Manifest
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresPermission
import com.danylom73.rescuehelper.R
import com.danylom73.rescuehelper.domain.alert.AlertController
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlertControllerImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : AlertController {

    private var mediaPlayer: MediaPlayer? = null
    private val vibrator: Vibrator? by lazy { context.getAppVibrator() }

    private var currentVolume: Float = 1f
    private var alertRunning: Boolean = false

    private val _isPlayingFlow = MutableStateFlow(false)
    override val isPlayingFlow: StateFlow<Boolean> = _isPlayingFlow.asStateFlow()

    override fun isPlaying(): Boolean = mediaPlayer?.isPlaying == true

    override fun startAlert(
        soundVolume: Float,
        withVibration: Boolean
    ) {
        currentVolume = soundVolume.coerceIn(0f, 1f)

        setSystemMediaVolume(context, currentVolume)

        val player = getOrCreateMediaPlayer()
        player.setVolume(currentVolume, currentVolume)

        if (!player.isPlaying) {
            player.start()
        }

        if (withVibration) {
            startVibration()
        }

        alertRunning = true
        _isPlayingFlow.value = true
    }

    override fun stopAlert() {
        mediaPlayer?.let { player ->
            if (player.isPlaying) {
                player.pause()
            }
            player.seekTo(0)
        }

        stopVibration()
        alertRunning = false
        _isPlayingFlow.value = false
    }

    override fun setSoundVolume(volume: Float) {
        currentVolume = volume.coerceIn(0f, 1f)
        mediaPlayer?.setVolume(currentVolume, currentVolume)
    }

    override fun isAlertRunning(): Boolean = alertRunning

    private fun getOrCreateMediaPlayer(): MediaPlayer {
        mediaPlayer?.let { return it }

        val player = MediaPlayer.create(context, R.raw.sound_alarm)?.apply {
            isLooping = true
            setVolume(currentVolume, currentVolume)

            setOnCompletionListener {
                _isPlayingFlow.value = false
                alertRunning = false
            }

            setOnErrorListener { _, _, _ ->
                _isPlayingFlow.value = false
                alertRunning = false
                true
            }
        } ?: throw RuntimeException("Failed to create MediaPlayer")

        mediaPlayer = player
        return player
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    private fun startVibration() {
        val vibrator = vibrator ?: return
        if (!vibrator.hasVibrator()) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val effect = VibrationEffect.createWaveform(
                longArrayOf(0, 700, 300, 700),
                0
            )
            vibrator.vibrate(effect)
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(longArrayOf(0, 700, 300, 700), 0)
        }
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    private fun stopVibration() {
        vibrator?.cancel()
    }

    private fun Context.getAppVibrator(): Vibrator? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            manager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    private fun setSystemMediaVolume(context: Context, fraction: Float) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val targetVolume = (maxVolume * fraction.coerceIn(0f, 1f)).toInt().coerceAtLeast(1)

        audioManager.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            targetVolume,
            0
        )
    }
}