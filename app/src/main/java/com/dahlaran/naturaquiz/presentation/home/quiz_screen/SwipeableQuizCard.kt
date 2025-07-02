package com.dahlaran.naturaquiz.presentation.home.quiz_screen

import QuizContent
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import com.dahlaran.naturaquiz.core.presentation.animation.SwipeAnimationSpecs
import com.dahlaran.naturaquiz.core.presentation.animation.SwipeAnimationState
import com.dahlaran.naturaquiz.core.presentation.animation.SwipeDirection
import com.dahlaran.naturaquiz.core.presentation.animation.rememberSwipeAnimationState
import com.dahlaran.naturaquiz.domain.entities.Plant
import com.dahlaran.naturaquiz.domain.entities.Quiz
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext
import kotlin.math.abs

@Composable
fun SwipeableQuizCard(
    quiz: Quiz,
    animationState: SwipeAnimationState,
    isOnTop: Boolean,
    zIndexValue: Float,
    onSwipe: (SwipeDirection) -> Unit,
    coroutineScope: CoroutineScope,
    swipeThreshold: Float,
) {
    QuizContent(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                translationX = animationState.offset.value.x
                translationY = animationState.offset.value.y
                rotationZ = animationState.rotation.value
                scaleX = animationState.scale.value
                scaleY = animationState.scale.value
            }
            .zIndex(zIndexValue)
            .pointerInput(Unit) {
                if (isOnTop) {
                    detectDragGestures(
                        onDragEnd = {
                            coroutineScope.launch {
                                if (abs(animationState.offset.value.x) > swipeThreshold) {
                                    val direction = if (animationState.offset.value.x > 0) {
                                        SwipeDirection.RIGHT
                                    } else {
                                        SwipeDirection.LEFT
                                    }
                                    onSwipe(direction)
                                } else {
                                    // Return to center
                                    animationState.offset.animateTo(
                                        Offset.Zero,
                                        SwipeAnimationSpecs.offsetSpringSpec
                                    )
                                    animationState.rotation.animateTo(
                                        0f,
                                        SwipeAnimationSpecs.defaultSpringSpec
                                    )
                                    animationState.scale.animateTo(
                                        1f,
                                        SwipeAnimationSpecs.defaultSpringSpec
                                    )
                                }
                            }
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            coroutineScope.launch {
                                awaitAll(
                                    async {
                                        animationState.offset.snapTo(
                                            animationState.offset.value + dragAmount
                                        )
                                    },
                                    async {
                                        animationState.rotation.snapTo(
                                            (animationState.offset.value.x * SwipeAnimationSpecs.ROTATION_FACTOR)
                                                .coerceIn(
                                                    -SwipeAnimationSpecs.MAX_ROTATION,
                                                    SwipeAnimationSpecs.MAX_ROTATION
                                                )
                                        )
                                    }
                                )
                            }
                        }
                    )
                }
            },
        quiz = quiz,
        onLeftClick = { if (isOnTop) onSwipe(SwipeDirection.LEFT) },
        onRightClick = { if (isOnTop) onSwipe(SwipeDirection.RIGHT) }
    )
}

@Preview
@Composable
fun SwipeableQuizCardPreview() {
    val quiz = Quiz(
        goodAnswer = Plant(
            1,
            "Good Plant",
            "Good Scientific Name",
            "https://d2seqvvyy3b8p2.cloudfront.net/60ea5b8f7de66e8e95be30b2989bfcbe.jpg",
            1999,
            "Family 1",
            "Genus 1"
        ), wrongAnswer = Plant(
            2, "Wrong Plant", "Wrong Scientific Name", null, 2000, "Family 2", "Genus 2"
        ), leftIsGoodAnswer = true
    )
    val animationState = rememberSwipeAnimationState(initialScale = 1f)
    SwipeableQuizCard(
        quiz = quiz,
        animationState = animationState,
        isOnTop = true,
        zIndexValue = 1f,
        onSwipe = {},
        coroutineScope = rememberCoroutineScope(),
        swipeThreshold = 100f
    )
}