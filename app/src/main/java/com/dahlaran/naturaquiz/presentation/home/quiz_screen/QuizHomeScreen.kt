package com.dahlaran.naturaquiz.presentation.home.quiz_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dahlaran.naturaquiz.presentation.viewmodel.QuizViewModel

@Composable
fun QuizHomeScreen(viewModel: QuizViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val quiz = state.quiz
    val nextQuiz = state.nextQuiz

    if (quiz != null && nextQuiz != null) {
        SwipeableQuizScreen(
            currentQuiz = quiz,
            nextQuiz = nextQuiz,
            streak = state.streak,
            onSwipeLeft = {
                viewModel.handleAnswer(isLeft = true)
            },
            onSwipeRight = {
                viewModel.handleAnswer(isLeft = false)
            }
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            if (state.isLoading) {
                Text("Loading...",
                    style = MaterialTheme.typography.bodyLarge,
                    )
            } else {
                Button(onClick = { viewModel.fetchPlants() }) {
                    Text("Fetch Plants")
                }
            }
        }
    }
}