package com.danylom73.rescuehelper.presentation.screen

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
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

    val multiplePermissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {
        viewModel.process(RequirementIntent.OnRequirementRefresh)
    }

    val bluetoothLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        viewModel.process(RequirementIntent.OnRequirementRefresh)
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
                val permissions = mutableListOf<String>()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    permissions += Manifest.permission.BLUETOOTH_SCAN
                    permissions += Manifest.permission.BLUETOOTH_CONNECT
                    permissions += Manifest.permission.BLUETOOTH_ADVERTISE
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissions += Manifest.permission.NEARBY_WIFI_DEVICES
                }

                multiplePermissionsLauncher.launch(
                    permissions.toTypedArray()
                )
            }

            RequirementType.LocationPermission -> {
                permissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }

            RequirementType.BluetoothEnabled -> {
                if (context as? Activity != null) {
                    val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    bluetoothLauncher.launch(intent)
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