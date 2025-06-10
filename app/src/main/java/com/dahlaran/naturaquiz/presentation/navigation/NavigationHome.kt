package com.dahlaran.naturaquiz.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.dahlaran.naturaquiz.presentation.home.home_list.HomeListScreen
import com.dahlaran.naturaquiz.presentation.home.quiz_screen.QuizHomeScreen
import com.dahlaran.naturaquiz.presentation.viewmodel.ListsViewModel
import com.dahlaran.naturaquiz.presentation.viewmodel.QuizViewModel
import kotlinx.serialization.Serializable

@Serializable
data object QuizHomeScreenKey : NavKey

@Serializable
data object HomeListScreenKey : NavKey


@Composable
fun NavigationHome(
    modifier: Modifier = Modifier,
    quizViewModel: QuizViewModel,
    listsViewModel: ListsViewModel
) {
    val backStack = rememberNavBackStack(QuizHomeScreenKey)

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
            rememberSceneSetupNavEntryDecorator()
        ),
        entryProvider =  { key ->
            when (key) {
                is QuizHomeScreenKey -> {
                    NavEntry(
                        key = key,
                        content = { QuizHomeScreen(quizViewModel) }
                    )
                }
                is HomeListScreenKey -> {
                    NavEntry(
                        key = key,
                        content = { HomeListScreen(listsViewModel) }
                    )
                }
                else -> throw IllegalArgumentException("Unknown screen: $key")
            }
        },
    )
}