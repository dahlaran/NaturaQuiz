package com.dahlaran.naturaquiz.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.dahlaran.naturaquiz.core.bus.Event
import com.dahlaran.naturaquiz.core.data.BaseViewModel
import com.dahlaran.naturaquiz.core.data.DataState
import com.dahlaran.naturaquiz.domain.usecases.GetQuizResponseUseCase
import com.dahlaran.naturaquiz.domain.usecases.GetPlantsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlantViewModel @Inject constructor(
    private val getPlantsUseCase: GetPlantsUseCase,
    private val getPlantResponseUseCase: GetQuizResponseUseCase
) : BaseViewModel() {

    private val _state = MutableStateFlow(PlantState())
    val state = _state.asStateFlow()

    fun fetchPlants() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            launchUsesCase(getPlantsUseCase.invoke(), onSuccess = { plants ->
                getPlantResponseUseCase.invoke(plants, null).let { getPlantResponse ->
                    if (getPlantResponse is DataState.Error) {
                        fetchPlants()
                    } else if (getPlantResponse is DataState.Success) {
                        _state.update {
                            it.copy(
                                plants = plants,
                                selectedQuiz = getPlantResponse.data,
                                isLoading = false
                            )
                        }
                        sendEvent(Event.NavigateToHomeScreen)
                    }
                }
            }, onError = { error ->
                _state.update { it.copy(isLoading = false, error = error) }
            })
        }
    }


    fun nextPlant() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getPlantResponseUseCase.invoke(_state.value.plants, _state.value.selectedQuiz).let { getPlantResponse ->
                if (getPlantResponse is DataState.Error) {
                    fetchPlants()
                } else if (getPlantResponse is DataState.Success) {
                    _state.update {
                        it.copy(
                            selectedQuiz = getPlantResponse.data,
                            isLoading = false
                        )
                    }
                    sendEvent(Event.NavigateToHomeScreen)
                }
            }
        }
    }


}