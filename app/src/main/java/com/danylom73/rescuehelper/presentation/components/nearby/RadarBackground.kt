package com.danylom73.rescuehelper.presentation.components.nearby

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import com.danylom73.rescuehelper.presentation.ui.theme.AppTheme

@Composable
fun RadarBackground(
    modifier: Modifier = Modifier,
    waveColor: Color = AppTheme.extendedColors.orangeBackground.copy(alpha = 0.7f),
    wavesCount: Int = 3,
    circleThickness: Dp = AppTheme.dimens.thicknessSmall,
    durationMillis: Int = 4000
) {
    val infiniteTransition = rememberInfiniteTransition()

    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val maxRadius = size.minDimension / 2f

        repeat(wavesCount) { index ->
            val waveProgress =
                (progress + index.toFloat() / wavesCount) % 1f

            val radius = maxRadius * waveProgress
            val alpha = (1f - waveProgress).coerceIn(0f, 1f)

            drawCircle(
                color = waveColor.copy(alpha = alpha),
                radius = radius,
                center = center,
                style = Stroke(width = circleThickness.toPx())
            )
        }
    }
}
