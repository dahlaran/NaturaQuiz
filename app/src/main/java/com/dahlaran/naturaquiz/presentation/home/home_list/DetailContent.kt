package com.dahlaran.naturaquiz.presentation.home.home_list

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import com.dahlaran.naturaquiz.domain.entities.Plant
import com.dahlaran.naturaquiz.domain.entities.Specie
import com.dahlaran.naturaquiz.presentation.home.home_list.plant_detail.PlantDetailScreen
import com.dahlaran.naturaquiz.presentation.home.home_list.specie_detail.SpecieDetailScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DetailContent(
    detailDisplay: HomeDetail,
    selectedPlant: Plant?,
    selectedSpecie: Specie?,
    animatedVisibilityScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
    onBackPressed: () -> Unit
) {
    when (detailDisplay) {
        HomeDetail.SPECIE_DETAIL -> {
            selectedSpecie?.let {
                SpecieDetailScreen(
                    specie = it,
                    animatedVisibilityScope = animatedVisibilityScope,
                    sharedTransitionScope = sharedTransitionScope,
                    onBackPressed = onBackPressed
                )
            }
        }

        HomeDetail.PLANT_DETAIL -> {
            selectedPlant?.let {
                PlantDetailScreen(
                    plant = it,
                    animatedVisibilityScope = animatedVisibilityScope,
                    sharedTransitionScope = sharedTransitionScope,
                    onBackPressed = onBackPressed
                )
            }
        }

        else -> Unit
    }
}