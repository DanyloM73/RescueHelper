package com.danylom73.rescuehelper.domain.nearby

enum class NearbyCommand {
    START_FLASHLIGHT_BLINKING,
    STOP_FLASHLIGHT_BLINKING,

    START_ALERT,
    STOP_ALERT,

    FLASHLIGHT_STATE_ON,
    FLASHLIGHT_STATE_OFF;

    companion object {
        fun fromName(name: String?): NearbyCommand? {
            if (name.isNullOrBlank()) return null

            return entries.firstOrNull {
                it.name.equals(name, ignoreCase = true)
            }
        }
    }
}