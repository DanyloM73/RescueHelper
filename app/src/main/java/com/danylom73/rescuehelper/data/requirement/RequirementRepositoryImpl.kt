package com.danylom73.rescuehelper.data.requirement

import android.Manifest
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.wifi.WifiManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.danylom73.rescuehelper.domain.requirement.Requirement
import com.danylom73.rescuehelper.domain.requirement.RequirementRepository
import com.danylom73.rescuehelper.domain.requirement.RequirementType
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequirementRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : RequirementRepository {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun check(): List<Requirement> = listOf(
        Requirement(
            RequirementType.NearbyPermission,
            hasNearbyPermission()
        ),
        Requirement(
            RequirementType.LocationPermission,
            hasLocationPermission()
        ),

        Requirement(
            RequirementType.BluetoothEnabled,
            isBluetoothEnabled()
        ),
        Requirement(
            RequirementType.LocationEnabled,
            isLocationEnabled()
        ),
        Requirement(
            RequirementType.WifiEnabled,
            isWiFiEnabled()
        )
    )

    // PERMISSIONS
    private fun hasNearbyPermission(): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.NEARBY_WIFI_DEVICES
            ) == PackageManager.PERMISSION_GRANTED
        } else true

    private fun hasLocationPermission(): Boolean =
        ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED


    // FEATURES
    private fun isBluetoothEnabled(): Boolean {
        val manager = context
            .getSystemService(
                BluetoothManager::class.java
            )
        return manager.adapter?.isEnabled == true
    }

    private fun isLocationEnabled(): Boolean {
        val manager = context
            .getSystemService(
                LocationManager::class.java
            )
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun isWiFiEnabled(): Boolean {
        val manager = context
            .getSystemService(
                WifiManager::class.java
            )
        return manager.isWifiEnabled
    }
}