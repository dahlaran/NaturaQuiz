package com.dahlaran.naturaquiz.presentation.home.home_list.layouts

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
    Column(
        modifier = Modifier.padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Species",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            maxLines = 1,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Bold,
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(species) { specie ->
                SpecieCard(specie = specie,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    onClick = { onSpecieSelected(specie) })
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
fun SpeciesListLayoutPreview() {
    val species = listOf(
        Specie(
            id = 1,
            name = "Specie 1",
            scientificName = "Scientific Name 1",
            imageUrl = "https://d2seqvvyy3b8p2.cloudfront.net/60ea5b8f7de66e8e95be30b2989bfcbe.jpg",
            discoveredYear = 2021,
            family = "Family 1",
            genus = "Genus 1",
        ),
        Specie(
            id = 2,
            name = "Specie 2",
            scientificName = "Scientific Name 2",
            imageUrl = "https://d2seqvvyy3b8p2.cloudfront.net/60ea5b8f7de66e8e95be30b2989bfcbe.jpg",
            discoveredYear = 2021,
            family = "Family 2",
            genus = "Genus 2",
        ),
    )
    MaterialTheme {
        SharedTransitionLayout {
            AnimatedContent(
                species, label = "preview species list"
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    SpeciesListLayout(species = it,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedContent,
                        onSpecieSelected = {})
                }
            }
        }
    }
}