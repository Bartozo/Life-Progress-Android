package com.bartozo.lifeprogress.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun CircleWaveAnimation(
    modifier: Modifier = Modifier,
    circleColor: Color = MaterialTheme.colorScheme.primary
) {
    val circles = listOf(
        remember { Animatable(0f) },
        remember { Animatable(0f) },
        remember { Animatable(0f) },
        remember { Animatable(0f) },
    )

    circles.forEachIndexed { index, animatable ->
        LaunchedEffect(animatable) {
            delay(index * 1000L)

            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(4000, easing = FastOutLinearInEasing),
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        circles.forEach { animatable ->
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.Center)
                    .graphicsLayer {
                        scaleX = animatable.value * 4 + 1
                        scaleY = animatable.value * 4 + 1
                        alpha = 1 - animatable.value
                    }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = circleColor, shape = CircleShape)
                )
            }
        }
    }
}