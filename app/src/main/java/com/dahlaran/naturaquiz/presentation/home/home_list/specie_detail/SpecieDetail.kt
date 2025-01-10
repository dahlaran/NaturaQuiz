package com.dahlaran.naturaquiz.presentation.home.home_list.specie_detail

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
import com.dahlaran.naturaquiz.domain.entities.Specie

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SpecieDetailScreen(
    specie: Specie,
    animatedVisibilityScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
    onBackPressed: () -> Unit,
) {
    BackHandler {
        onBackPressed()
    }
    with(sharedTransitionScope) {
        val boundsKey = "bounds_specie${specie.id}"
        val imageKey = "image_specie${specie.id}"
        val nameKey = "name_specie${specie.id}"

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
                    text = specie.getNotNullName(),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.sharedElement(
                        rememberSharedContentState(key = nameKey),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                )
            }

            AsyncImage(
                model = specie.imageUrl,
                contentDescription = specie.getNotNullName(),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .sharedElement(rememberSharedContentState(key = imageKey),
                        animatedVisibilityScope = animatedVisibilityScope,

                        boundsTransform = { _, _ ->
                            spring(
                                dampingRatio = 0.8f, stiffness = 380f
                            )
                        })
                    .clip(MaterialTheme.shapes.medium),
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (specie.getNotNullName() == specie.name && specie.scientificName != null) {
                Text(
                    text = specie.scientificName,
                    style = MaterialTheme.typography.headlineMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontStyle = FontStyle.Italic,
                )
            }

            SpecieInformation(specie = specie)
        }
    }
}

@Composable
private fun SpecieInformation(specie: Specie) {
    specie.family?.let {
        Text(
            text = "Family: $it",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
    specie.genus?.let {
        Text(
            text = "Genus: $it",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
    specie.discoveredYear?.let {
        Text(
            text = "Discovered: $it",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}