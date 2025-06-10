package com.dahlaran.naturaquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dahlaran.naturaquiz.presentation.home.HomeScreen
import com.dahlaran.naturaquiz.presentation.navigation.NavigationHome
import com.dahlaran.naturaquiz.presentation.splash.SplashScreen
import com.dahlaran.naturaquiz.presentation.viewmodel.ListsViewModel
import com.dahlaran.naturaquiz.presentation.viewmodel.QuizViewModel
import com.dahlaran.naturaquiz.ui.theme.NaturaQuizTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { false }

        enableEdgeToEdge()
        setContent {
            NaturaQuizTheme {
                Navigation()
            }
        }
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val quizViewModel: QuizViewModel = hiltViewModel()
    val listsViewModel: ListsViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(
                quizViewModel = quizViewModel,
                listsViewModel = listsViewModel,
                navController = navController
            )
        }
        composable("home") {
            NavigationHome(quizViewModel = quizViewModel, listsViewModel = listsViewModel)
            HomeScreen(quizViewModel = quizViewModel, listsViewModel = listsViewModel)
        }
    }
}