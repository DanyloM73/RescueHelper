package com.danylom73.rescuehelper.presentation.components.nearby

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.danylom73.rescuehelper.presentation.ui.theme.AppTheme

@Composable
fun SideGlowOverlay(
    modifier: Modifier = Modifier,
    color: Color = AppTheme.colors.primary,
    pulseEnabled: Boolean = false,
    minAlpha: Float = 0.18f,
    maxAlpha: Float = 0.55f,
    durationMillis: Int = 1200,
    edgeWidth: Dp = AppTheme.dimens.spacingLarge,
    innerGlowFraction: Float = 0.35f,
    animationEasing: Easing = FastOutSlowInEasing
) {
    val animatedAlpha = if (pulseEnabled) {
        val transition = rememberInfiniteTransition()
        val value by transition.animateFloat(
            initialValue = minAlpha,
            targetValue = maxAlpha,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = durationMillis,
                    easing = animationEasing
                ),
                repeatMode = RepeatMode.Reverse
            )
        )
        value
    } else {
        maxAlpha
    }

    val middleAlpha = (animatedAlpha * innerGlowFraction).coerceIn(0f, 1f)

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(edgeWidth)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                color.copy(alpha = animatedAlpha),
                                color.copy(alpha = middleAlpha),
                                Color.Transparent
                            )
                        )
                    )
            )

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(edgeWidth)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                color.copy(alpha = middleAlpha),
                                color.copy(alpha = animatedAlpha)
                            )
                        )
                    )
            )
        }
    }
}