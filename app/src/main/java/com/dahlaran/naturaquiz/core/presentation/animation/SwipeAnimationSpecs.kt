package com.dahlaran.naturaquiz.core.presentation.animation

import androidx.compose.animation.core.spring
import androidx.compose.ui.geometry.Offset

object SwipeAnimationSpecs {
    val defaultSpringSpec = spring<Float>(
        dampingRatio = 0.8f,
        stiffness = 100f,
        visibilityThreshold = 0.001f
    )

    val offsetSpringSpec = spring<Offset>(
        dampingRatio = 0.8f,
        stiffness = 100f,
        visibilityThreshold = Offset(0.001f, 0.001f)
    )

    // Animation constants
    const val ROTATION_FACTOR = 0.1f
    const val MAX_ROTATION = 15f
    const val SWIPE_THRESHOLD_DP = 100f
    const val ANIMATION_DELAY_MS = 300L
}