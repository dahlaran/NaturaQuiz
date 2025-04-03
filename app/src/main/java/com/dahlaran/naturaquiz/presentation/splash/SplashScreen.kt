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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.dahlaran.naturaquiz.core.bus.Event
import com.dahlaran.naturaquiz.core.bus.EventBus
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

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(key1 = lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            EventBus.events.collect { event ->
                when (event) {
                    is Event.ToastError -> {
                        event.error.showUsingCodeOnly(context)
                    }

                    is Event.NavigateToHomeScreen -> {
                        navController.navigate("home") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                }
            }
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            quizViewModel.state.value.error?.let {
                // Display an error
                Button(onClick = { quizViewModel.onEvent(QuizViewEvent.Refresh) }) {
                    Text("Retry")
                }
            } ?: run {
                // Display a loading indicator
                CircularProgressIndicator()
            }
        }
    }
}