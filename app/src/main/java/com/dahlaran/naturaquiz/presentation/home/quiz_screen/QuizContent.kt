import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.dahlaran.naturaquiz.core.presentation.button.QuizButton
import com.dahlaran.naturaquiz.domain.entities.Plant
import com.dahlaran.naturaquiz.domain.entities.Quiz

@Composable
fun QuizContent(
    modifier: Modifier = Modifier,
    quiz: Quiz,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
) {
    Box(
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        // Background Image
        AsyncImage(
            modifier = Modifier
                .fillMaxSize(),
            model = quiz.goodAnswer.imageUrl,
            contentDescription = "Plant image",
            contentScale = ContentScale.Crop,
        )

        // Button row at the bottom
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Left Answer Button
            QuizButton(
                onClick = onLeftClick,
                modifier = Modifier
                    .weight(1f),
                text = quiz.getLeftAnswerText()
            )


            QuizButton(
                onClick = onRightClick,
                modifier = Modifier
                    .weight(1f),
                text = quiz.getRightAnswerText()
            )
        }
    }
}

@Preview
@Composable
fun QuizScreenPreview() {
    QuizContent(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        quiz = Quiz(
            goodAnswer = Plant(
                1, "Good Plant", "Good Scientific Name", "https://www.example.com/image.jpg"
            ), wrongAnswer = Plant(
                2, "Wrong Plant", "Wrong Scientific Name", "https://www.example.com/image.jpg"
            ), leftIsGoodAnswer = true
        ),
        onLeftClick = {}, onRightClick = {},
    )
}