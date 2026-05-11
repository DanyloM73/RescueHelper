package com.danylom73.rescuehelper.domain.notification

import android.app.Notification
import com.danylom73.rescuehelper.domain.requirement.RequirementType

interface NotificationController {
    fun createNotificationChannel()
    fun buildForegroundNotification(): Notification
    fun showStoppedByRequirementNotification(type: RequirementType)
}