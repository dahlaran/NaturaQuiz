package com.dahlaran.naturaquiz.presentation.home.home_list.plant_detail

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.dahlaran.naturaquiz.domain.entities.Plant

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PlantDetailScreen(
    plant: Plant,
    animatedVisibilityScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
    onBackPressed: () -> Unit,
) {
    BackHandler {
        onBackPressed()
    }
    with(sharedTransitionScope) {
        val boundsKey = "bounds_plant${plant.id}"
        val imageKey = "image_plant${plant.id}"
        val nameKey = "scientificName_plant${plant.id}"
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .sharedBounds(
                    rememberSharedContentState(key = boundsKey),
                    animatedVisibilityScope = animatedVisibilityScope,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackPressed,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = plant.scientificName ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.sharedElement(
                        rememberSharedContentState(key = nameKey),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                )
            }

            AsyncImage(
                model = plant.imageUrl,
                contentDescription = plant.getNotNullName(),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .sharedElement(
                        rememberSharedContentState(key = imageKey),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            spring(
                                dampingRatio = 0.8f,
                                stiffness = 380f
                            )
                        }
                    )
                    .clip(MaterialTheme.shapes.medium),
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (plant.name?.isNotEmpty() == true) {
                Text(
                    text = plant.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            PlantInformation(plant = plant)
        }
    }
}

@Composable
private fun PlantInformation(plant: Plant) {
    plant.family?.let {
        Text(
            text = "Family: $it",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
    plant.genus?.let {
        Text(
            text = "Genus: $it",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
    plant.discoveredYear?.let {
        Text(
            text = "Discovered: $it",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}