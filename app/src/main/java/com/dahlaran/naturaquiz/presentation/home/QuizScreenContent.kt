package com.dahlaran.naturaquiz.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.dahlaran.naturaquiz.R
import com.dahlaran.naturaquiz.data.model.Plant
import com.dahlaran.naturaquiz.data.model.SelectedQuiz

@Composable
fun QuizScreenContent(
    modifier: Modifier = Modifier,
    quiz: SelectedQuiz,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
) {
    Box(
        modifier = modifier
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(8.dp),
            placeholder = painterResource(R.drawable.placeholder),
            model = quiz.goodAnswer.imageUrl,
            contentDescription = "Plant image"
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(32.dp)
        ) {
            Button(
                onClick = {
                    onLeftClick()
                },
            ) {
                Text(quiz.getLeftAnswerText())
            }
            Button(
                onClick = {
                    onRightClick()
                },
            ) {
                Text(quiz.getRightAnswerText())
            }
        }
    }
}

@Preview
@Composable
fun QuizScreenPreview() {
    QuizScreenContent(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        quiz = SelectedQuiz(
            goodAnswer = Plant(
                1, "Good Plant", "Good Scientific Name", "https://www.example.com/image.jpg"
            ), wrongAnswer = Plant(
                2, "Wrong Plant", "Wrong Scientific Name", "https://www.example.com/image.jpg"
            ), leftIsGoodAnswer = true
        ), onLeftClick = {}, onRightClick = {},
    )
}