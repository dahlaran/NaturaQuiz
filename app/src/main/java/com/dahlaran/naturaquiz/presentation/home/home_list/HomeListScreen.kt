package com.dahlaran.naturaquiz.presentation.home.home_list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dahlaran.naturaquiz.domain.entities.Plant
import com.dahlaran.naturaquiz.domain.entities.Specie
import com.dahlaran.naturaquiz.presentation.home.home_list.layouts.HomeListErrorLayout
import com.dahlaran.naturaquiz.presentation.viewmodel.ListsViewModel


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HomeListScreen(listsViewModel: ListsViewModel) {
    var selectedPlant by remember { mutableStateOf<Plant?>(null) }
    var selectedSpecie by remember { mutableStateOf<Specie?>(null) }
    var detailDisplay by remember { mutableStateOf<HomeDetail>(HomeDetail.NONE) }
    val state by listsViewModel.state.collectAsStateWithLifecycle()

    SharedTransitionLayout {
        AnimatedContent(
            detailDisplay,
            label = "list_transition"
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    state.error != null -> HomeListErrorLayout(
                        error = state.error!!,
                        onRetry = { listsViewModel.fetchLists() }
                    )
                    it == HomeDetail.NONE -> HomeListContent(
                        state = state,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedContent,
                        onPlantSelected = { plant ->
                            selectedPlant = plant
                            detailDisplay = HomeDetail.PLANT_DETAIL
                        },
                        onSpecieSelected = { specie ->
                            selectedSpecie = specie
                            detailDisplay = HomeDetail.SPECIE_DETAIL
                        },
                        onRefresh = { listsViewModel.fetchLists() }
                    )
                    else -> DetailContent(
                        detailDisplay = detailDisplay,
                        selectedPlant = selectedPlant,
                        selectedSpecie = selectedSpecie,
                        animatedVisibilityScope = this@AnimatedContent,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        onBackPressed = { detailDisplay = HomeDetail.NONE }
                    )
                }
            }
        }
    }
}