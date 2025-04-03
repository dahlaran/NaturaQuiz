package com.dahlaran.naturaquiz.presentation.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.dahlaran.naturaquiz.core.presentation.view.ErrorView
import com.dahlaran.naturaquiz.presentation.viewmodel.ListsViewEvent
import com.dahlaran.naturaquiz.presentation.viewmodel.ListsViewModel
import com.dahlaran.naturaquiz.presentation.viewmodel.QuizViewEvent
import com.dahlaran.naturaquiz.presentation.viewmodel.QuizViewModel

/**
 * SplashScreen composable that will display a loading indicator while fetching the plants
 */
@Composable
fun SplashScreen(
    quizViewModel: QuizViewModel,
    listsViewModel: ListsViewModel,
    navController: NavHostController
) {
    LaunchedEffect(Unit) {
        quizViewModel.onEvent(QuizViewEvent.OnArriveOnSplash)
        listsViewModel.onEvent(ListsViewEvent.OnArriveOnSplash)
    }
    val state by quizViewModel.state.collectAsStateWithLifecycle()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {

            state.quiz?.let {
                navController.navigate("home") {
                    popUpTo("splash") { inclusive = true }
                }
            }

            state.error?.let {
                ErrorView(error = it, onRetry = {
                    quizViewModel.onEvent(QuizViewEvent.Refresh)
                })
            } ?: run {
                // Display a loading indicator
                CircularProgressIndicator()
            }
        }
    }
}