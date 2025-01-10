package com.dahlaran.naturaquiz.presentation.home.home_list

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import com.dahlaran.naturaquiz.domain.entities.Plant
import com.dahlaran.naturaquiz.domain.entities.Specie
import com.dahlaran.naturaquiz.presentation.home.home_list.layouts.PlantsListLayout
import com.dahlaran.naturaquiz.presentation.home.home_list.layouts.SpeciesListLayout
import com.dahlaran.naturaquiz.presentation.viewmodel.ListsHomeState

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeListContent(
    state: ListsHomeState,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedContentScope,
    onPlantSelected: (Plant) -> Unit,
    onSpecieSelected: (Specie) -> Unit,
    onRefresh: () -> Unit
) {
    val refreshing = state.isLoading
    PullToRefreshBox(
        isRefreshing = refreshing,
        onRefresh = onRefresh
    ) {
        Column {
            SpeciesListLayout(
                species = state.species ?: emptyList(),
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
                onSpecieSelected = onSpecieSelected
            )

            PlantsListLayout(
                plants = state.plants ?: emptyList(),
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
                onPlantSelected = onPlantSelected
            )
        }
    }
}