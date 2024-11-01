package com.dahlaran.naturaquiz.core.presentation.animation
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.VectorConverter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset

data class SwipeAnimationState(
    val offset: Animatable<Offset, AnimationVector2D>,
    val rotation: Animatable<Float, AnimationVector1D>,
    val scale: Animatable<Float, AnimationVector1D>
)

@Composable
fun rememberSwipeAnimationState(
    initialScale: Float = 1f
): SwipeAnimationState {
    val offset = remember { Animatable(Offset.Zero, Offset.VectorConverter) }
    val rotation = remember { Animatable(0f) }
    val scale = remember { Animatable(initialScale) }

    return remember {
        SwipeAnimationState(offset, rotation, scale)
    }
}