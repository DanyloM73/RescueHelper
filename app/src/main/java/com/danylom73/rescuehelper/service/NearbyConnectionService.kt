package com.danylom73.rescuehelper.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import com.danylom73.rescuehelper.data.notification.NotificationControllerImpl
import com.danylom73.rescuehelper.domain.nearby.NearbyRuntimeController
import com.danylom73.rescuehelper.domain.notification.NotificationController
import com.danylom73.rescuehelper.domain.requirement.RequirementMonitor
import com.danylom73.rescuehelper.domain.requirement.RequirementType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NearbyConnectionService : Service() {

    @Inject
    lateinit var runtimeController: NearbyRuntimeController

    @Inject
    lateinit var requirementMonitor: RequirementMonitor

    @Inject
    lateinit var notificationController: NotificationController

    private var stoppedByUser: Boolean = false
    private var stoppedBecauseOfRequirement: Boolean = false

    override fun onCreate() {
        super.onCreate()
        notificationController.createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                stoppedByUser = false
                stoppedBecauseOfRequirement = false

                startAsForeground()
                runtimeController.startForCurrentRole()

                requirementMonitor.start { failedRequirementType ->
                    handleCriticalRequirementLost(failedRequirementType)
                }
            }

            ACTION_STOP -> {
                stoppedByUser = true
                stopNearbyService(showReasonNotification = false)
            }
        }

        return START_STICKY
    }

    override fun onDestroy() {
        requirementMonitor.stop()

        if (stoppedByUser || stoppedBecauseOfRequirement) {
            runtimeController.stop()
        }

        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startAsForeground() {
        val notification = notificationController.buildForegroundNotification()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceCompat.startForeground(
                this,
                NotificationControllerImpl.FOREGROUND_NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE
            )
        } else {
            startForeground(
                NotificationControllerImpl.FOREGROUND_NOTIFICATION_ID,
                notification
            )
        }
    }

    private fun handleCriticalRequirementLost(type: RequirementType) {
        stoppedBecauseOfRequirement = true

        stopNearbyService(
            showReasonNotification = true,
            failedRequirementType = type
        )
    }

    private fun stopNearbyService(
        showReasonNotification: Boolean,
        failedRequirementType: RequirementType? = null
    ) {
        requirementMonitor.stop()

        runtimeController.stop(showReasonNotification)

        if (showReasonNotification && failedRequirementType != null) {
            notificationController.showStoppedByRequirementNotification(
                failedRequirementType
            )
        }

        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    companion object {
        const val ACTION_START = "com.danylom73.rescuehelper.START_NEARBY_SERVICE"
        const val ACTION_STOP = "com.danylom73.rescuehelper.STOP_NEARBY_SERVICE"

        fun start(context: Context) {
            val intent = Intent(context, NearbyConnectionService::class.java)
                .setAction(ACTION_START)

            ContextCompat.startForegroundService(context, intent)
        }

        fun stop(context: Context) {
            val intent = Intent(context, NearbyConnectionService::class.java)
                .setAction(ACTION_STOP)

            context.startService(intent)
        }
    }
}