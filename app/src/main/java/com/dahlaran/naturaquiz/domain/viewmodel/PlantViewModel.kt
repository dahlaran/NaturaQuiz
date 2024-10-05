package com.dahlaran.naturaquiz.domain.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dahlaran.naturaquiz.core.BaseViewModel
import com.dahlaran.naturaquiz.domain.usecases.GetPlantsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlantViewModel @Inject constructor(
    private val getPlantsUseCase: GetPlantsUseCase
) : BaseViewModel() {

    var state by mutableStateOf(PlantState())

    fun fetchPlants() {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            launchUsesCase(getPlantsUseCase.invoke(),
                onSuccess = {
                    state = state.copy(plants = it, plantSelected = it.first(), isLoading = false)
                }, onError = {
                    state = state.copy(isLoading = false)
                })
        }
    }


    fun nextPlant() {
        state.plants?.let { plants ->
            val index = plants.indexOf(state.plantSelected)
            if (index != -1 && plants.size < index) {
                state = state.copy(plantSelected = plants[index + 1])
            } else {
                fetchPlants()
            }
        } ?: run {
            fetchPlants()
        }
    }
}