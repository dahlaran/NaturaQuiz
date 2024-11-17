package com.dahlaran.naturaquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dahlaran.naturaquiz.presentation.home.HomeScreen
import com.dahlaran.naturaquiz.presentation.splash.SplashScreen
import com.dahlaran.naturaquiz.presentation.viewmodel.QuizViewModel
import com.dahlaran.naturaquiz.ui.theme.NaturaQuizTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            NaturaQuizTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation(innerPadding)
                }
            }
        }
    }
}

@Composable
fun Navigation(innerPadding: PaddingValues) {
    val navController = rememberNavController()
    val viewModel: QuizViewModel = hiltViewModel()
    NavHost(
        modifier = Modifier.padding(innerPadding),
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(viewModel = viewModel, navController = navController)
        }
        composable("home") {
            HomeScreen(viewModel = viewModel)
        }
    }
}