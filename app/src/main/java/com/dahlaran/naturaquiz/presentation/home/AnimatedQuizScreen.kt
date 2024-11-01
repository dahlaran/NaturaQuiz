import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.dahlaran.naturaquiz.data.model.SelectedQuiz
import com.dahlaran.naturaquiz.presentation.home.QuizScreenContent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AnimatedQuizScreen(
    currentQuiz: SelectedQuiz,
    onLeft: () -> Unit,
    onRight: () -> Unit,
) {
    var isAnimating: Boolean by remember { mutableStateOf(false) }
    var previousQuiz: SelectedQuiz? by remember { mutableStateOf(null) }
    var fallDirection = remember { FallDirection.LEFT }

    val scale = remember { Animatable(1f) }
    val zIndex = remember { Animatable(1f) }

    // Previous screen animation values
    val prevRotation = remember { Animatable(0f) }
    val prevYTranslation = remember { Animatable(0f) }
    val prevXTranslation = remember { Animatable(0f) }

    LaunchedEffect(currentQuiz) {
        // Display animation only if there was a previous screen
        if (previousQuiz != null) {
            isAnimating = true

            // Reset animation
            prevRotation.snapTo(0f)
            prevXTranslation.snapTo(0f)
            prevYTranslation.snapTo(0f)

            // Start with new screen slightly scaled down in background
            scale.snapTo(0.9f)
            zIndex.snapTo(0f)

            // Animate previous screen falling
            launch {
                prevRotation.animateTo(
                    targetValue = if (fallDirection == FallDirection.RIGHT) 90f else -90f,
                    animationSpec = tween(
                        durationMillis = 2000,
                        easing = FastOutSlowInEasing
                    )
                )
            }

            launch {
                prevYTranslation.animateTo(
                    targetValue = 1000f,
                    animationSpec = tween(
                        durationMillis = 2000,
                        easing = FastOutSlowInEasing
                    )
                )
            }

            launch {
                prevXTranslation.animateTo(
                    targetValue = if (fallDirection == FallDirection.RIGHT) 500f else -500f,
                    animationSpec = tween(
                        durationMillis = 2000,
                        easing = FastOutSlowInEasing
                    )
                )
            }

            // Wait for falling animation to be mostly complete
            delay(1200)

            // Bring new screen to foreground
            launch {
                zIndex.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(300)
                )
            }

            launch {
                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                )
            }

            delay(300)
            isAnimating = false
        }
        previousQuiz = currentQuiz
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Current screen (starts in background, moves to foreground)
        QuizScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                }
                .zIndex(zIndex.value),
            quiz = currentQuiz,
            onLeftClick = {
                if (!isAnimating) {
                    fallDirection = FallDirection.LEFT
                    onLeft()
                }
            },
            onRightClick = {
                if (!isAnimating) {
                    fallDirection = FallDirection.RIGHT
                    onRight()
                }
            },

            )

        // Previous screen (falls away), disable its click events
        if (isAnimating && previousQuiz != null) {
            QuizScreenContent(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        rotationZ = prevRotation.value
                        translationY = prevYTranslation.value
                        translationX = prevXTranslation.value
                        transformOrigin = TransformOrigin(
                            pivotFractionX = if (fallDirection == FallDirection.RIGHT) 0f else 1f,
                            pivotFractionY = 1f
                        )
                    }
                    .zIndex(2f - zIndex.value),
                quiz = previousQuiz!!,
                onLeftClick = {},
                onRightClick = {},
                )
        }
    }
}

enum class FallDirection {
    LEFT, RIGHT
}