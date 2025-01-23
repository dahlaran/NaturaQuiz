package com.dahlaran.naturaquiz.presentation.home.quiz_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
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

    state.quiz?.let {
        SwipeableQuizScreen(
            currentQuiz = it,
            nextQuiz = state.nextQuiz!!,
            streak = state.streak,
            onSwipeLeft = {
                viewModel.handleAnswer(isLeft = true)
            },
            onSwipeRight = {
                viewModel.handleAnswer(isLeft = false)
            }
        )
    } ?: run {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    Text("Loading...")
                }
            } else {
                Button(onClick = { viewModel.fetchPlants() }) {
                    Text("Fetch Plants")
                }
            }
        }
    }
}