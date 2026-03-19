package com.danylom73.rescuehelper.domain.requirement

import androidx.annotation.StringRes
import com.danylom73.rescuehelper.R

enum class RequirementType(
    val category: RequirementCategory,
    @StringRes val titleRes: Int,
    val isOptional: Boolean
) {
    NearbyPermission(
        RequirementCategory.Permission,
        R.string.requirement_nearby_permission_title,
        false
    ),
    LocationPermission(
        RequirementCategory.Permission,
        R.string.requirement_location_permission_title,
        false
    ),
    BluetoothEnabled(
        RequirementCategory.Feature,
        R.string.requirement_bluetooth_feature_title,
        false
    ),
    LocationEnabled(
        RequirementCategory.Feature,
        R.string.requirement_location_feature_title,
        false
    ),
    WifiEnabled(
        RequirementCategory.Feature,
        R.string.requirement_wifi_feature_title,
        true
    )
}