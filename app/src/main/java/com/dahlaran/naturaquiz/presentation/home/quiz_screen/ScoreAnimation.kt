package com.dahlaran.naturaquiz.presentation.home.quiz_screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ScoreAnimation(
    isCorrect: Boolean?,
    onAnimationComplete: () -> Unit = {}
) {
    var animationTriggered by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (animationTriggered) 0f else 0.6f,
        animationSpec = tween(durationMillis = 500),
        finishedListener = {
            onAnimationComplete()
        },
        label = "alpha"
    )

    LaunchedEffect(isCorrect) {
        if (isCorrect != null) {
            animationTriggered = false
            kotlinx.coroutines.delay(250)
            animationTriggered = true
        }
    }

    if (isCorrect != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { this.alpha = alpha }
                .background(if (isCorrect) Color(0xFF4CAF50) else Color(0xFFBF3131))
        )
    }
}

@Preview
@Composable
fun ScoreAnimationPreview() {
    ScoreAnimation(isCorrect = true)
}