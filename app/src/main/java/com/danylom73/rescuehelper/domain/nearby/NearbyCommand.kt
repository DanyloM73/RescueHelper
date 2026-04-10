package com.danylom73.rescuehelper.domain.nearby

enum class NearbyCommand {
    TURN_ON_FLASHLIGHT,
    TURN_OFF_FLASHLIGHT,

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