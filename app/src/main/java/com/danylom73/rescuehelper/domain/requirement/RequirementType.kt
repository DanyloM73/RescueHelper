package com.danylom73.rescuehelper.domain.requirement

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material.icons.outlined.Bluetooth
import androidx.compose.material.icons.outlined.Radar
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.ui.graphics.vector.ImageVector
import com.danylom73.rescuehelper.R

enum class RequirementType(
    val category: RequirementCategory,
    @StringRes val titleRes: Int,
    val isOptional: Boolean,
    val icon: ImageVector
) {
    NearbyPermission(
        RequirementCategory.Permission,
        R.string.requirement_nearby_permission_title,
        false,
        Icons.Outlined.Radar
    ),
    LocationPermission(
        RequirementCategory.Permission,
        R.string.requirement_location_permission_title,
        false,
        Icons.Filled.LocationSearching
    ),
    BluetoothEnabled(
        RequirementCategory.Feature,
        R.string.requirement_bluetooth_feature_title,
        false,
        Icons.Outlined.Bluetooth
    ),
    LocationEnabled(
        RequirementCategory.Feature,
        R.string.requirement_location_feature_title,
        false,
        Icons.Filled.LocationOn
    ),
    WifiEnabled(
        RequirementCategory.Feature,
        R.string.requirement_wifi_feature_title,
        true,
        Icons.Outlined.Wifi
    )
}