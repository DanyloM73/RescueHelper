package com.danylom73.rescuehelper.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.danylom73.rescuehelper.presentation.screen.NearbyScreen
import com.danylom73.rescuehelper.presentation.ui.theme.RescueHelperTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RescueHelperTheme {
                Scaffold (
                    modifier = Modifier.fillMaxSize()
                ) { paddingValues ->
                    NearbyScreen(Modifier.padding(paddingValues))
                }
            }
        }
    }
}