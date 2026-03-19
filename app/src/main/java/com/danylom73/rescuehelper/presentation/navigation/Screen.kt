package com.danylom73.rescuehelper.presentation.navigation

sealed class Screen(val route: String) {
    object RequirementScreen : Screen("requirement")
    object NearbyScreen : Screen("nearby")
}