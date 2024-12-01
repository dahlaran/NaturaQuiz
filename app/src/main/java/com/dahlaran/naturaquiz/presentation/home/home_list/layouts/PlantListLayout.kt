package com.dahlaran.naturaquiz.presentation.home.home_list.layouts

import androidx.compose.foundation.lazy.items
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dahlaran.naturaquiz.domain.entities.Plant
import com.dahlaran.naturaquiz.presentation.home.home_list.cards.PlantCard

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PlantsListLayout(
    plants: List<Plant>,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedContentScope,
    onPlantSelected: (Plant) -> Unit,
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(plants) { plant ->
            PlantCard(
                plant = plant,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
                onClick = { onPlantSelected(plant) },
            )
        }
    }
}