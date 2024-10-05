package com.dahlaran.naturaquiz.presentation.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.dahlaran.naturaquiz.domain.viewmodel.PlantViewModel

@Composable
fun SplashScreen(viewModel: PlantViewModel, navController: NavHostController) {
    LaunchedEffect(Unit) {
        viewModel.fetchPlants()
    }
    if (viewModel.state.plants != null) {
        navController.navigate("home")
    }

    // Display a loading indicator or splash image
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}