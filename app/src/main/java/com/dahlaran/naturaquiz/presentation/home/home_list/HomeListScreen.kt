package com.dahlaran.naturaquiz.presentation.home.home_list

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dahlaran.naturaquiz.presentation.home.home_list.layouts.HomeListErrorLayout
import com.dahlaran.naturaquiz.presentation.home.home_list.plant_detail.PlantDetailScreen
import com.dahlaran.naturaquiz.presentation.home.home_list.specie_detail.SpecieDetailScreen
import com.dahlaran.naturaquiz.presentation.viewmodel.ListsViewEvent
import com.dahlaran.naturaquiz.presentation.viewmodel.ListsViewModel


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HomeListScreen(listsViewModel: ListsViewModel) {
    val state by listsViewModel.state.collectAsStateWithLifecycle()

    SharedTransitionLayout {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = "list"
        ) {
            composable("list") {
                HomeListContent(
                    state = state,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable,
                    onPlantSelected = { plant ->
                        navController.navigate("detail/plant/${plant.id}")
                    },
                    onSpecieSelected = { specie ->
                        navController.navigate("detail/specie/${specie.id}")
                    },
                    onRefresh = { listsViewModel.onEvent(ListsViewEvent.Refresh) },
                )
            }

            composable(
                "detail/plant/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) {
                val id = it.arguments?.getInt("id")
                state.plants?.find { it.id == id }?.let { plant ->
                    PlantDetailScreen(
                        plant = plant,
                        animatedVisibilityScope = this@composable,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        onBackPressed = { navController.navigateUp() }
                    )
                } ?: run {
                    navController.navigate("error")
                }
            }

            composable(
                "detail/specie/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) {
                val id = it.arguments?.getInt("id")
                state.species?.find { it.id == id }?.let { specie ->
                    SpecieDetailScreen(
                        specie = specie,
                        animatedVisibilityScope = this@composable,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        onBackPressed = { navController.navigateUp() }
                    )
                } ?: run {
                    navController.navigate("error")
                }
            }

            composable("error") {
                HomeListErrorLayout(
                    error = state.error!!,
                    onRetry = { listsViewModel.onEvent(ListsViewEvent.Refresh) },
                )
            }
        }
    }
}