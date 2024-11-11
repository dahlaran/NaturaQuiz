import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.dahlaran.naturaquiz.data.model.Quiz
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun SwipeableQuizScreen(
    currentQuiz: Quiz,
    nextQuiz: Quiz?,
    onLeft: () -> Unit,
    onRight: () -> Unit,
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()

    // Track which card is on top
    var isFirstCardOnTop by remember { mutableStateOf(true) }

    // Animation states for both cards
    val firstCardOffset = remember { Animatable(Offset.Zero, Offset.VectorConverter) }
    val firstCardRotation = remember { Animatable(0f) }
    val firstCardScale = remember { Animatable(1f) }

    val secondCardOffset = remember { Animatable(Offset.Zero, Offset.VectorConverter) }
    val secondCardRotation = remember { Animatable(0f) }
    val secondCardScale = remember { Animatable(0.9f) }

    // Constants
    val swipeThreshold = with(density) { 100.dp.toPx() }
    val rotationFactor = 0.1f
    val maxRotation = 15f

    // Function to animate swipe
    fun animateSwipe(direction: SwipeDirection) {
        coroutineScope.launch {
            val endX = size.width * 1.5f * direction.multiplier
            val targetRotation = maxRotation * direction.multiplier

            val animationSpec = spring<Float>(
                dampingRatio = 0.8f,
                stiffness = 100f,
                visibilityThreshold = 0.001f
            )

            val offsetAnimSpec = spring<Offset>(
                dampingRatio = 0.8f,
                stiffness = 100f,
                visibilityThreshold = Offset(0.001f, 0.001f)
            )

            // Animate top card away
            val topCardOffset = if (isFirstCardOnTop) firstCardOffset else secondCardOffset
            val topCardRotation = if (isFirstCardOnTop) firstCardRotation else secondCardRotation
            val topCardScale = if (isFirstCardOnTop) firstCardScale else secondCardScale

            // Animate bottom card to full size
            val bottomCardScale = if (isFirstCardOnTop) secondCardScale else firstCardScale

            launch {
                topCardOffset.animateTo(
                    Offset(endX, 0f),
                    offsetAnimSpec
                )
            }
            launch {
                topCardRotation.animateTo(
                    targetRotation,
                    animationSpec
                )
            }
            launch {
                topCardScale.animateTo(
                    0.8f,
                    animationSpec
                )
            }
            launch {
                bottomCardScale.animateTo(
                    1f,
                    animationSpec
                )
            }

            kotlinx.coroutines.delay(300)

            // Trigger callback and prepare for next card
            when (direction) {
                SwipeDirection.LEFT -> onLeft()
                SwipeDirection.RIGHT -> onRight()
            }

            // Reset the top card's position instantly (it will be hidden behind the new top card)
            topCardOffset.snapTo(Offset.Zero)
            topCardRotation.snapTo(0f)
            topCardScale.snapTo(0.9f)

            // Swap which card is on top
            isFirstCardOnTop = !isFirstCardOnTop
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { size = it.size }
    ) {
        // First card
        QuizContent(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationX = firstCardOffset.value.x
                    translationY = firstCardOffset.value.y
                    rotationZ = firstCardRotation.value
                    scaleX = firstCardScale.value
                    scaleY = firstCardScale.value
                }
                .zIndex(if (isFirstCardOnTop) 1f else 0f)
                .pointerInput(Unit) {
                    if (isFirstCardOnTop) {
                        detectDragGestures(
                            onDragEnd = {
                                coroutineScope.launch {
                                    if (abs(firstCardOffset.value.x) > swipeThreshold) {
                                        val direction = if (firstCardOffset.value.x > 0) {
                                            SwipeDirection.RIGHT
                                        } else {
                                            SwipeDirection.LEFT
                                        }
                                        animateSwipe(direction)
                                    } else {
                                        // Return to center
                                        firstCardOffset.animateTo(Offset.Zero, spring())
                                        firstCardRotation.animateTo(0f, spring())
                                        firstCardScale.animateTo(1f, spring())
                                    }
                                }
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                coroutineScope.launch {
                                    firstCardOffset.snapTo(firstCardOffset.value + dragAmount)
                                    firstCardRotation.snapTo((firstCardOffset.value.x * rotationFactor)
                                        .coerceIn(-maxRotation, maxRotation))
                                }
                            }
                        )
                    }
                },
            quiz = if (isFirstCardOnTop) currentQuiz else (nextQuiz ?: currentQuiz),
            onLeftClick = { if (isFirstCardOnTop) animateSwipe(SwipeDirection.LEFT) },
            onRightClick = { if (isFirstCardOnTop) animateSwipe(SwipeDirection.RIGHT) }
        )

        // Second card
        QuizContent(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationX = secondCardOffset.value.x
                    translationY = secondCardOffset.value.y
                    rotationZ = secondCardRotation.value
                    scaleX = secondCardScale.value
                    scaleY = secondCardScale.value
                }
                .zIndex(if (!isFirstCardOnTop) 1f else 0f)
                .pointerInput(Unit) {
                    if (!isFirstCardOnTop) {
                        detectDragGestures(
                            onDragEnd = {
                                coroutineScope.launch {
                                    if (abs(secondCardOffset.value.x) > swipeThreshold) {
                                        val direction = if (secondCardOffset.value.x > 0) {
                                            SwipeDirection.RIGHT
                                        } else {
                                            SwipeDirection.LEFT
                                        }
                                        animateSwipe(direction)
                                    } else {
                                        // Return to center
                                        secondCardOffset.animateTo(Offset.Zero, spring())
                                        secondCardRotation.animateTo(0f, spring())
                                        secondCardScale.animateTo(1f, spring())
                                    }
                                }
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                coroutineScope.launch {
                                    secondCardOffset.snapTo(secondCardOffset.value + dragAmount)
                                    secondCardRotation.snapTo((secondCardOffset.value.x * rotationFactor)
                                        .coerceIn(-maxRotation, maxRotation))
                                }
                            }
                        )
                    }
                },
            quiz = if (!isFirstCardOnTop) currentQuiz else (nextQuiz ?: currentQuiz),
            onLeftClick = { if (!isFirstCardOnTop) animateSwipe(SwipeDirection.LEFT) },
            onRightClick = { if (!isFirstCardOnTop) animateSwipe(SwipeDirection.RIGHT) }
        )
    }
}

private enum class SwipeDirection(val multiplier: Float) {
    LEFT(-1f),
    RIGHT(1f)
}