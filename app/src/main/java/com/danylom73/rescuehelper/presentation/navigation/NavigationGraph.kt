package com.danylom73.rescuehelper.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.danylom73.rescuehelper.presentation.screen.NearbyScreen
import com.danylom73.rescuehelper.presentation.screen.RequirementScreen

@Composable
fun NavigationGraph(
    navHostController: NavHostController,
    modifier: Modifier
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.RequirementScreen.route
    ) {
        composable(Screen.RequirementScreen.route) {
            RequirementScreen(modifier = modifier) { navTo ->
                if (navTo == Screen.NearbyScreen.route) {
                    navHostController.navigate(navTo) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                } else {
                    navHostController.navigate(navTo)
                }
            }
        }

        composable(Screen.NearbyScreen.route) {
            NearbyScreen(modifier = modifier)
        }
    }
}