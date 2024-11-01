package com.dahlaran.naturaquiz.presentation.home

import AnimatedQuizScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dahlaran.naturaquiz.data.model.Plant
import com.dahlaran.naturaquiz.presentation.viewmodel.PlantViewModel

@Composable
fun HomeScreen(viewModel: PlantViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    state.selectedQuiz?.let {
        AnimatedQuizScreen(currentQuiz = it, onLeft = {
            viewModel::nextPlant
        }, onRight = {
            viewModel::nextPlant
        })
    } ?: run {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    Text("Loading...")
                }
            } else {
                Button(onClick = { viewModel.fetchPlants() }) {
                    Text("Fetch Plants")
                }
            }
        }
    }
}