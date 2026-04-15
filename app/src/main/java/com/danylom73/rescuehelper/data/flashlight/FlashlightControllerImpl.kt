package com.danylom73.rescuehelper.data.flashlight

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import com.danylom73.rescuehelper.domain.flashlight.FlashlightController
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlashlightControllerImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : FlashlightController {

    private val cameraManager =
        context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

    private val _isFlashlightEnabled = MutableStateFlow(false)
    override val isFlashlightEnabled: StateFlow<Boolean> = _isFlashlightEnabled.asStateFlow()

    private val cameraId: String? by lazy {
        cameraManager.cameraIdList.firstOrNull { id ->
            val characteristics = cameraManager.getCameraCharacteristics(id)
            val hasFlash = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
            val lensFacing = characteristics.get(CameraCharacteristics.LENS_FACING)
            hasFlash && lensFacing == CameraCharacteristics.LENS_FACING_BACK
        }
    }

    private var blinkingJob: Job? = null

    private val torchCallback = object : CameraManager.TorchCallback() {
        override fun onTorchModeChanged(id: String, enabled: Boolean) {
            if (id == cameraId) {
                _isFlashlightEnabled.value = enabled
            }
        }

        override fun onTorchModeUnavailable(id: String) {
            if (id == cameraId) {
                _isFlashlightEnabled.value = false
            }
        }
    }

    init {
        cameraManager.registerTorchCallback(torchCallback, null)
    }

    override fun setEnabled(enabled: Boolean) {
        stopBlinking()
        cameraId?.let { cameraManager.setTorchMode(it, enabled) }
    }

    override fun startBlinking(intervalMillis: Long) {
        if (blinkingJob?.isActive == true) return

        blinkingJob = CoroutineScope(SupervisorJob() + Dispatchers.Default).launch {
            while (isActive) {
                cameraId?.let { cameraManager.setTorchMode(it, true) }
                delay(intervalMillis)
                if (!isActive) break
                cameraId?.let { cameraManager.setTorchMode(it, false) }
                delay(intervalMillis)
            }
        }
    }

    override fun stopBlinking() {
        blinkingJob?.cancel()
        blinkingJob = null
    }
}