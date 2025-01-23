package com.dahlaran.naturaquiz.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dahlaran.naturaquiz.presentation.home.home_list.HomeListScreen
import com.dahlaran.naturaquiz.presentation.home.navigation_bar.BottomNavBar
import com.dahlaran.naturaquiz.presentation.home.navigation_bar.BottomNavScreen
import com.dahlaran.naturaquiz.presentation.home.quiz_screen.QuizHomeScreen
import com.dahlaran.naturaquiz.presentation.viewmodel.ListsViewModel
import com.dahlaran.naturaquiz.presentation.viewmodel.QuizViewModel

@Composable
fun HomeScreen(quizViewModel: QuizViewModel, listsViewModel: ListsViewModel) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            NavHost(
                navController = navController,
                startDestination = BottomNavScreen.Quiz.route
            ) {
                composable(BottomNavScreen.Quiz.route) {
                    QuizHomeScreen(quizViewModel)
                }
                composable(BottomNavScreen.List.route) {
                    HomeListScreen(listsViewModel)
                }
            }
        }
    }
}