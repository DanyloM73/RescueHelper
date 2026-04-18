package com.danylom73.rescuehelper.presentation.components.base

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.danylom73.rescuehelper.presentation.ui.theme.AppTheme
import com.danylom73.rescuehelper.presentation.ui.theme.RescueHelperTheme

@Composable
fun BaseSnackbarHost(
    hostState: SnackbarHostState
) {
    Box(modifier = Modifier.fillMaxSize()) {
        SnackbarHost(
            hostState = hostState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = AppTheme.dimens.spacingExtraLarge)
        ) { data ->
            BaseSnackbar(data.visuals as? BaseSnackbarVisuals)
        }
    }
}

@Composable
fun BaseSnackbar(
    visuals: BaseSnackbarVisuals?
) {
    AnimatedVisibility(
        visible = true,
        enter = slideInVertically(
            initialOffsetY = { -it }
        ) + fadeIn(),
        exit = slideOutVertically(
            targetOffsetY = { -it }
        ) + fadeOut()
    ) {
        Row(
            modifier = Modifier
                .padding(
                    horizontal = AppTheme.dimens.spacingRegular,
                    vertical = AppTheme.dimens.spacingSmall
                )
                .fillMaxWidth()
                .clip(RoundedCornerShape(AppTheme.dimens.radiusRegular))
                .background(AppTheme.extendedColors.whiteBackground)
                .border(
                    AppTheme.dimens.thicknessSmall,
                    AppTheme.colors.primary,
                    RoundedCornerShape(AppTheme.dimens.radiusRegular)
                )
                .padding(AppTheme.dimens.spacingRegular),
            verticalAlignment = Alignment.CenterVertically
        ) {
            visuals?.icon?.let { icon ->
                Box(Modifier.size(AppTheme.dimens.iconSizeMedium)) {
                    Icon(
                        imageVector = icon.vector,
                        contentDescription = null,
                        tint = AppTheme.colors.primary

                    )
                }
                Spacer(Modifier.width(AppTheme.dimens.spacingSmall))
            }

            Text(
                text = visuals?.message ?: "",
                style = AppTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = AppTheme.colors.primary,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

class BaseSnackbarVisuals(
    override val message: String,
    val icon: BaseSnackbarIcon? = null
) : SnackbarVisuals {
    override val actionLabel: String? = null
    override val withDismissAction: Boolean = false
    override val duration: SnackbarDuration = SnackbarDuration.Short
}

enum class BaseSnackbarIcon(val vector: ImageVector) {
    INFO(Icons.Outlined.Info),
    ERROR(Icons.Outlined.Close),
    CONNECTED(Icons.Default.Link),
    DISCONNECTED(Icons.Default.LinkOff)
}

@Preview
@Composable
fun BaseSnackbarPreviewWithoutIcon() {
    RescueHelperTheme {
        BaseSnackbar(
            BaseSnackbarVisuals(
                message = "Some message"
            )
        )
    }
}

@Preview
@Composable
fun BaseSnackbarPreviewWithIcon() {
    RescueHelperTheme {
        BaseSnackbar(
            BaseSnackbarVisuals(
                message = "Some info message",
                icon = BaseSnackbarIcon.INFO
            )
        )
    }
}