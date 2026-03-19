package com.danylom73.rescuehelper.presentation.components.requirement.previews

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.danylom73.rescuehelper.domain.requirement.Requirement
import com.danylom73.rescuehelper.domain.requirement.RequirementCategory
import com.danylom73.rescuehelper.domain.requirement.RequirementType
import com.danylom73.rescuehelper.mvi.requirement.RequirementState
import com.danylom73.rescuehelper.presentation.components.requirement.RequirementComposable
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
private fun RequirementsNotGrantedPreview() {
    RescueHelperTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            RequirementComposable(
                state = RequirementState(
                    requirements = mapOf(
                        RequirementCategory.Permission to listOf(
                            Requirement(RequirementType.LocationPermission, true),
                            Requirement(RequirementType.NearbyPermission, false)
                        ),
                        RequirementCategory.Feature to listOf(
                            Requirement(RequirementType.BluetoothEnabled, false),
                            Requirement(RequirementType.LocationEnabled, true),
                            Requirement(RequirementType.WifiEnabled, false)
                        )
                    )
                ),
                onRequirementClick = {},
                onContinueClick = {}
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
private fun RequirementsGrantedPreview() {
    RescueHelperTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            RequirementComposable(
                state = RequirementState(
                    requirements = mapOf(
                        RequirementCategory.Permission to listOf(
                            Requirement(RequirementType.LocationPermission, true),
                            Requirement(RequirementType.NearbyPermission, true)
                        ),
                        RequirementCategory.Feature to listOf(
                            Requirement(RequirementType.BluetoothEnabled, true),
                            Requirement(RequirementType.LocationEnabled, true),
                            Requirement(RequirementType.WifiEnabled, false)
                        )
                    )
                ),
                onRequirementClick = {},
                onContinueClick = {}
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
private fun RequirementsEmptyPreview() {
    RescueHelperTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            RequirementComposable(
                state = RequirementState(
                    requirements = emptyMap()
                ),
                onRequirementClick = {},
                onContinueClick = {}
            )
        }
    }
}