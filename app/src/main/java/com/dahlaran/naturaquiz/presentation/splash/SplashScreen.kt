package com.dahlaran.naturaquiz.presentation.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import com.dahlaran.naturaquiz.presentation.viewmodel.QuizViewModel

/**
 * SplashScreen composable that will display a loading indicator while fetching the plants
 */
@Composable
fun SplashScreen(viewModel: QuizViewModel, navController: NavHostController) {
    LaunchedEffect(Unit) {
        viewModel.fetchPlants()
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

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        viewModel.state.value.error?.let {
            // Display an error
            Button(onClick = { viewModel.fetchPlants() }) {
                Text("Retry")
            }
        } ?: run {
            // Display a loading indicator
            CircularProgressIndicator()
        }
    }
}