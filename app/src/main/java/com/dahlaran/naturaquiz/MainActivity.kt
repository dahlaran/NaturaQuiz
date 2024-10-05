package com.dahlaran.naturaquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dahlaran.naturaquiz.presentation.home.HomeScreen
import com.dahlaran.naturaquiz.domain.viewmodel.PlantViewModel
import com.dahlaran.naturaquiz.presentation.splash.SplashScreen
import com.dahlaran.naturaquiz.ui.theme.NaturaQuizTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NaturaQuizTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation()
                }
            }
        }
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(viewModel = hiltViewModel(), navController = navController)
        }
        composable("home") {
            val viewModel: PlantViewModel = hiltViewModel()
            val plant = viewModel.state.plantSelected

            if (plant != null) {
                HomeScreen(plant, viewModel::nextPlant)
            }
        }
    }
}