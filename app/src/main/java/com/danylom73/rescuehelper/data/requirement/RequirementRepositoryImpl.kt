package com.danylom73.rescuehelper.data.requirement

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.wifi.WifiManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.danylom73.rescuehelper.domain.requirement.Requirement
import com.danylom73.rescuehelper.domain.requirement.RequirementRepository
import com.danylom73.rescuehelper.domain.requirement.RequirementType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequirementRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : RequirementRepository {

    override fun check(): List<Requirement> = listOf(
        Requirement(
            RequirementType.NearbyPermission,
            hasNearbyPermission() && hasBluetoothPermission()
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

    override fun observeRequirements(): Flow<List<Requirement>> = callbackFlow {
        fun sendSnapshot() {
            trySend(check())
        }

        sendSnapshot()

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                sendSnapshot()
            }
        }

        val filter = IntentFilter().apply {
            addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            addAction(LocationManager.MODE_CHANGED_ACTION)
            addAction(LocationManager.PROVIDERS_CHANGED_ACTION)
            addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
        }

        ContextCompat.registerReceiver(
            context,
            receiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

        awaitClose {
            context.unregisterReceiver(receiver)
        }
    }

    private fun hasNearbyPermission(): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.NEARBY_WIFI_DEVICES
            ) == PackageManager.PERMISSION_GRANTED
        } else true

    private fun hasLocationPermission(): Boolean =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    private fun hasBluetoothPermission(): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_ADVERTISE
            ) == PackageManager.PERMISSION_GRANTED
        } else true

    private fun isBluetoothEnabled(): Boolean {
        val manager = context.getSystemService(BluetoothManager::class.java)
        return manager.adapter?.isEnabled == true
    }

    private fun isLocationEnabled(): Boolean {
        val manager = context.getSystemService(LocationManager::class.java)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            manager.isLocationEnabled
        } else {
            manager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        }
    }

    private fun isWiFiEnabled(): Boolean {
        val manager = context.applicationContext.getSystemService(WifiManager::class.java)
        return manager.isWifiEnabled
    }
}