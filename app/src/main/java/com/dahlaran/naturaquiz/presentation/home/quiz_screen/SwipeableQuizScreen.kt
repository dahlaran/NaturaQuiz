package com.dahlaran.naturaquiz.presentation.home.quiz_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.dahlaran.naturaquiz.core.presentation.animation.SwipeAnimationSpecs
import com.dahlaran.naturaquiz.core.presentation.animation.SwipeDirection
import com.dahlaran.naturaquiz.core.presentation.animation.rememberSwipeAnimationState
import com.dahlaran.naturaquiz.domain.entities.Quiz
import kotlinx.coroutines.launch

@Composable
fun SwipeableQuizScreen(
    currentQuiz: Quiz,
    nextQuiz: Quiz?,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()
    var isFirstCardOnTop by remember { mutableStateOf(true) }

    // Animation states for both cards, reduce the second card's scale to 0.9f to make it smaller (its better)
    val firstCardState = rememberSwipeAnimationState(initialScale = 1f)
    val secondCardState = rememberSwipeAnimationState(initialScale = 0.9f)

    val swipeThreshold = with(density) { SwipeAnimationSpecs.SWIPE_THRESHOLD_DP.dp.toPx() }

    fun animateSwipe(direction: SwipeDirection) {
        coroutineScope.launch {
            val endX = size.width * 1.5f * direction.multiplier
            val targetRotation = SwipeAnimationSpecs.MAX_ROTATION * direction.multiplier

            val topCardState = if (isFirstCardOnTop) firstCardState else secondCardState
            val bottomCardState = if (isFirstCardOnTop) secondCardState else firstCardState

            // Animate top card away
            launch {
                topCardState.offset.animateTo(
                    Offset(endX, 0f),
                    SwipeAnimationSpecs.offsetSpringSpec
                )
            }
            launch {
                topCardState.rotation.animateTo(
                    targetRotation,
                    SwipeAnimationSpecs.defaultSpringSpec
                )
            }
            launch {
                topCardState.scale.animateTo(
                    0.8f,
                    SwipeAnimationSpecs.defaultSpringSpec
                )
            }

            // Animate bottom card to full size
            launch {
                bottomCardState.scale.animateTo(
                    1f,
                    SwipeAnimationSpecs.defaultSpringSpec
                )
            }

            kotlinx.coroutines.delay(SwipeAnimationSpecs.ANIMATION_DELAY_MS)

            when (direction) {
                SwipeDirection.LEFT -> onSwipeLeft()
                SwipeDirection.RIGHT -> onSwipeRight()
            }

            // Reset the off-screen card
            topCardState.offset.snapTo(Offset.Zero)
            topCardState.rotation.snapTo(0f)
            topCardState.scale.snapTo(0.9f)

            isFirstCardOnTop = !isFirstCardOnTop
        }
    }

    // Display two cards to prevent recreation of the card when currentQuiz changes
    // When a card is swiped, the next card is displayed
    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { size = it.size }
    ) {
        // First card
        SwipeableQuizCard(
            quiz = if (isFirstCardOnTop) currentQuiz else (nextQuiz ?: currentQuiz),
            animationState = firstCardState,
            isOnTop = isFirstCardOnTop,
            zIndexValue = if (isFirstCardOnTop) 1f else 0f,
            onSwipe = { direction -> animateSwipe(direction) },
            coroutineScope = coroutineScope,
            swipeThreshold = swipeThreshold
        )

        // Second card
        SwipeableQuizCard(
            quiz = if (!isFirstCardOnTop) currentQuiz else (nextQuiz ?: currentQuiz),
            animationState = secondCardState,
            isOnTop = !isFirstCardOnTop,
            zIndexValue = if (!isFirstCardOnTop) 1f else 0f,
            onSwipe = { direction -> animateSwipe(direction) },
            coroutineScope = coroutineScope,
            swipeThreshold = swipeThreshold
        )
    }
}