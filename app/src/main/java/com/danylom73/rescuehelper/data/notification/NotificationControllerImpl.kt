package com.danylom73.rescuehelper.data.notification

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.danylom73.rescuehelper.R
import com.danylom73.rescuehelper.domain.notification.NotificationController
import com.danylom73.rescuehelper.domain.requirement.RequirementType
import com.danylom73.rescuehelper.presentation.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationControllerImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : NotificationController {

    override fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.nearby_service_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        )

        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    override fun buildForegroundNotification(): Notification {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(context.getString(R.string.nearby_service_description))
            .setOngoing(true)
            .setAutoCancel(false)
            .setOnlyAlertOnce(true)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(createOpenAppPendingIntent())
            .build()
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun showStoppedByRequirementNotification(type: RequirementType) {
        if (!canShowNotifications()) return

        val message = when (type) {
            RequirementType.NearbyPermission ->
                context.getString(R.string.nearby_stopped_permissions_lost)

            RequirementType.LocationPermission ->
                context.getString(R.string.nearby_stopped_location_permission_lost)

            RequirementType.NotificationPermission ->
                context.getString(R.string.nearby_stopped_notification_permission_lost)

            RequirementType.BluetoothEnabled ->
                context.getString(R.string.nearby_stopped_bluetooth_disabled)

            RequirementType.LocationEnabled ->
                context.getString(R.string.nearby_stopped_location_disabled)

            RequirementType.WifiEnabled ->
                context.getString(R.string.nearby_stopped_wifi_disabled)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(createOpenAppPendingIntent())
            .build()

        NotificationManagerCompat.from(context)
            .notify(STOPPED_NOTIFICATION_ID, notification)
    }

    private fun createOpenAppPendingIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        return PendingIntent.getActivity(
            context,
            OPEN_APP_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun canShowNotifications(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    companion object {
        const val CHANNEL_ID = "nearby_connection_service"
        const val FOREGROUND_NOTIFICATION_ID = 1001
        const val STOPPED_NOTIFICATION_ID = 1002

        private const val OPEN_APP_REQUEST_CODE = 2001
    }
}