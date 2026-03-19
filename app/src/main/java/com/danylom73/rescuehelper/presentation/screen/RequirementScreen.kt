package com.danylom73.rescuehelper.presentation.screen

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.danylom73.rescuehelper.domain.requirement.Requirement
import com.danylom73.rescuehelper.domain.requirement.RequirementType
import com.danylom73.rescuehelper.mvi.requirement.RequirementIntent
import com.danylom73.rescuehelper.mvi.requirement.RequirementViewModel
import com.danylom73.rescuehelper.presentation.components.requirement.RequirementComposable
import com.danylom73.rescuehelper.presentation.navigation.Screen

@Composable
fun RequirementScreen(
    modifier: Modifier = Modifier,
    viewModel: RequirementViewModel = hiltViewModel(),
    navigate: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        viewModel.process(RequirementIntent.OnRequirementRefresh)
    }

    val bluetoothLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        viewModel.process(RequirementIntent.OnRequirementRefresh)
    }

    val bluetoothPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            bluetoothLauncher.launch(intent)
        } else {
            viewModel.process(RequirementIntent.OnRequirementRefresh)
        }
    }

    val settingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        viewModel.process(RequirementIntent.OnRequirementRefresh)
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.process(RequirementIntent.OnRequirementRefresh)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    fun processRequirement(requirement: Requirement) {
        when (requirement.type) {
            RequirementType.NearbyPermission -> {
                permissionLauncher.launch(
                    Manifest.permission.NEARBY_WIFI_DEVICES
                )
            }

            RequirementType.LocationPermission -> {
                permissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }

            RequirementType.BluetoothEnabled -> {
                if (context as? Activity != null) {
                    if (hasBluetoothConnectPermission(context)) {
                        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                        bluetoothLauncher.launch(intent)
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            bluetoothPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)
                        }
                    }
                }
            }

            RequirementType.LocationEnabled -> {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                settingsLauncher.launch(intent)
            }

            RequirementType.WifiEnabled -> {
                val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)
                } else {
                    Intent(Settings.ACTION_WIFI_SETTINGS)
                }
                settingsLauncher.launch(intent)
            }
        }
    }

    RequirementComposable(
        modifier = modifier,
        state = state,
        onRequirementClick = { processRequirement(it) },
        onContinueClick = { navigate(Screen.NearbyScreen.route) }
    )
}

private fun hasBluetoothConnectPermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.BLUETOOTH_CONNECT
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }
}