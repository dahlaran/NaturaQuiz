package com.dahlaran.naturaquiz.presentation.home.quiz_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dahlaran.naturaquiz.core.data.AppError
import com.dahlaran.naturaquiz.core.presentation.view.ErrorView
import com.dahlaran.naturaquiz.presentation.viewmodel.QuizViewEvent
import com.dahlaran.naturaquiz.presentation.viewmodel.QuizViewModel

@Composable
fun QuizHomeScreen(viewModel: QuizViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            state.error != null -> {
                ErrorView(
                    error = state.error!!,
                    onRetry = { viewModel.onEvent(QuizViewEvent.Refresh) }
                )
            }

            state.quiz != null && state.nextQuiz != null -> {
                SwipeableQuizScreen(
                    currentQuiz = state.quiz!!,
                    nextQuiz = state.nextQuiz!!,
                    streak = state.streak,
                    onSwipeLeft = { viewModel.onEvent(QuizViewEvent.HandelAnswer(isLeft = true)) },
                    onSwipeRight = { viewModel.onEvent(QuizViewEvent.HandelAnswer(isLeft = false)) }
                )
            }

            else -> {
                ErrorView(
                    error = AppError.UnexpectedError(),
                    onRetry = { viewModel.onEvent(QuizViewEvent.Refresh) }
                )
            }
        }
    }
}