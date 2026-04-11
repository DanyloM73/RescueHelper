package com.danylom73.rescuehelper.presentation.components.nearby.previews

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.danylom73.rescuehelper.domain.nearby.NearbyHost
import com.danylom73.rescuehelper.mvi.nearby.NearbyState
import com.danylom73.rescuehelper.presentation.components.nearby.NearbyComposable
import com.danylom73.rescuehelper.presentation.screen.config.NearbyScreenUiConfig
import com.danylom73.rescuehelper.presentation.ui.theme.AppTheme
import com.danylom73.rescuehelper.presentation.ui.theme.RescueHelperTheme

@Preview(
    name = "Light",
    showBackground = true
)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ResponderNoConnectPreview() {
    RescueHelperTheme {
        Surface(
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
            color = Color.Transparent
        ) {
            NearbyComposable(
                config = object : NearbyScreenUiConfig {
                    override val showHosts: Boolean = true
                    override val canDisconnect: Boolean = true
                },
                state = NearbyState()
            )
        }
    }
}

@Preview(
    name = "Light",
    showBackground = true
)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ResponderConnectedPreview() {
    RescueHelperTheme {
        Surface(
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
            color = Color.Transparent
        ) {
            NearbyComposable(
                config = object : NearbyScreenUiConfig {
                    override val showHosts: Boolean = true
                    override val canDisconnect: Boolean = true
                },
                state = NearbyState(
                    connectedEndpointId = "1234"
                )
            )
        }
    }
}

@Preview(
    name = "Light",
    showBackground = true
)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ResponderSearchHostAvailablePreview() {
    RescueHelperTheme {
        Surface(
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
            color = Color.Transparent
        ) {
            NearbyComposable(
                config = object : NearbyScreenUiConfig {
                    override val showHosts: Boolean = true
                    override val canDisconnect: Boolean = true
                },
                state = NearbyState(
                    isDiscovering = true,
                    discoveredHosts = listOf(
                        NearbyHost("1234", "Samsung S24"),
                        NearbyHost("4321", "Poco F4"),
                        NearbyHost("5678", "Xiaomi Redmi Note 5"),
                    )
                )
            )
        }
    }
}

@Preview(
    name = "Light",
    showBackground = true
)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ResponderSearchHostUnavailablePreview() {
    RescueHelperTheme {
        Surface(
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
            color = Color.Transparent
        ) {
            NearbyComposable(
                config = object : NearbyScreenUiConfig {
                    override val showHosts: Boolean = true
                    override val canDisconnect: Boolean = true
                },
                state = NearbyState(
                    isDiscovering = true
                )
            )
        }
    }
}

@Preview(
    name = "Light",
    showBackground = true
)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun UserNoConnectPreview() {
    RescueHelperTheme {
        Surface(
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
            color = Color.Transparent
        ) {
            NearbyComposable(
                config = object : NearbyScreenUiConfig {
                    override val showHosts: Boolean = false
                    override val canDisconnect: Boolean = false
                },
                state = NearbyState()
            )
        }
    }
}

@Preview(
    name = "Light",
    showBackground = true
)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun UserConnectedPreview() {
    RescueHelperTheme {
        Surface(
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
            color = Color.Transparent
        ) {
            NearbyComposable(
                config = object : NearbyScreenUiConfig {
                    override val showHosts: Boolean = false
                    override val canDisconnect: Boolean = false
                },
                state = NearbyState(
                    connectedEndpointId = "4321"
                )
            )
        }
    }
}

@Preview(
    name = "Light",
    showBackground = true
)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun UserConnectedFlashlightEnabledPreview() {
    RescueHelperTheme {
        Surface(
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
            color = Color.Transparent
        ) {
            NearbyComposable(
                config = object : NearbyScreenUiConfig {
                    override val showHosts: Boolean = false
                    override val canDisconnect: Boolean = false
                },
                state = NearbyState(
                    connectedEndpointId = "4321"
                ),
                isFlashLightEnabled = true
            )
        }
    }
}

@Preview(
    name = "Light",
    showBackground = true
)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun UserHostEnabledPreview() {
    RescueHelperTheme {
        Surface(
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
            color = Color.Transparent
        ) {
            NearbyComposable(
                config = object : NearbyScreenUiConfig {
                    override val showHosts: Boolean = false
                    override val canDisconnect: Boolean = false
                },
                state = NearbyState(
                    isAdvertising = true
                )
            )
        }
    }
}
