package com.danylom73.rescuehelper.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.danylom73.rescuehelper.presentation.navigation.NavigationGraph
import com.danylom73.rescuehelper.presentation.ui.theme.AppTheme
import com.danylom73.rescuehelper.presentation.ui.theme.RescueHelperTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            RescueHelperTheme {
                Scaffold (
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.linearGradient(
                                colorStops = arrayOf(
                                    0.5f to AppTheme.colors.background,
                                    0.9f to AppTheme.extendedColors.purpleBackground,
                                    1f to AppTheme.extendedColors.orangeBackground
                                )
                            )
                        ),
                    containerColor = Color.Transparent
                ) { paddingValues ->
                    NavigationGraph(
                        navHostController = navController,
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }
    }
}