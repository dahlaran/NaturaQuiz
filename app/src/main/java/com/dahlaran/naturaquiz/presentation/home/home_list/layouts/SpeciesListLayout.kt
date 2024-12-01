package com.dahlaran.naturaquiz.presentation.home.home_list.layouts

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dahlaran.naturaquiz.domain.entities.Specie
import com.dahlaran.naturaquiz.presentation.home.home_list.cards.SpecieCard

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SpeciesListLayout(
    species: List<Specie>,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedContentScope,
    onSpecieSelected: (Specie) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(species) { specie ->
            SpecieCard(
                specie = specie,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
                onClick = { onSpecieSelected(specie) }
            )
        }
    }
}