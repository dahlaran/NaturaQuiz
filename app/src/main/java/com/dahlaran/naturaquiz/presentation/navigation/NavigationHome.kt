package com.dahlaran.naturaquiz.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.dahlaran.naturaquiz.presentation.home.home_list.HomeListScreen
import com.dahlaran.naturaquiz.presentation.home.navigation_bar.BottomNavBar
import com.dahlaran.naturaquiz.presentation.home.quiz_screen.QuizHomeScreen
import com.dahlaran.naturaquiz.presentation.navigation.Screen.HomeListScreenKey
import com.dahlaran.naturaquiz.presentation.navigation.Screen.QuizHomeScreenKey
import com.dahlaran.naturaquiz.presentation.viewmodel.ListsViewModel
import com.dahlaran.naturaquiz.presentation.viewmodel.QuizViewModel
import kotlinx.serialization.Serializable


@Serializable
sealed class Screen : NavKey {
    @Serializable
    data object QuizHomeScreenKey : Screen()

    @Serializable
    data object HomeListScreenKey : Screen()
}

@Composable
fun NavigationHome(
    modifier: Modifier = Modifier,
    quizViewModel: QuizViewModel,
    listsViewModel: ListsViewModel
) {
    val backStack: NavBackStack = rememberNavBackStack(QuizHomeScreenKey)

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentNavKey = backStack.last(),
                onNavigate = { navKey ->
                    if (backStack.last() != navKey) {
                        if (backStack.last() in listOf(QuizHomeScreenKey, HomeListScreenKey)) {
                            backStack.removeLastOrNull()
                        }
                        backStack.add(navKey)
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier.padding(padding)
        ) {
            NavDisplay(
                modifier = modifier,
                backStack = backStack,
                entryDecorators = listOf(
                    rememberSavedStateNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                    rememberSceneSetupNavEntryDecorator()
                ),
                entryProvider = entryProvider {
                    entry<Screen.QuizHomeScreenKey> {
                        QuizHomeScreen(quizViewModel)
                    }

                    entry<Screen.HomeListScreenKey> {
                        HomeListScreen(listsViewModel)
                    }
                },
            )
        }
    }
}