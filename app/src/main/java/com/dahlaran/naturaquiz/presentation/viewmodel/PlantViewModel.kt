package com.dahlaran.naturaquiz.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.dahlaran.naturaquiz.core.bus.Event
import com.dahlaran.naturaquiz.core.data.BaseViewModel
import com.dahlaran.naturaquiz.core.data.DataState
import com.dahlaran.naturaquiz.data.model.Plant
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

    /**
     * Fetch plants from the API and store them in the state
     */
    fun fetchPlants() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            launchUsesCase(getPlantsUseCase.invoke(), onSuccess = { plants ->
                createQuizResponse(plants)
            }, onError = { error ->
                _state.update { it.copy(isLoading = false, error = error) }
            })
        }
    }


    fun handleAnswer(isLeft: Boolean) {
        // Check if the answer is correct
        val isCorrect = state.value.quiz?.let { quiz ->
            if (isLeft) quiz.leftIsGoodAnswer else !quiz.leftIsGoodAnswer
        } ?: false

        // TODO : Create score and store it

        nextPlant()
    }

    private fun nextPlant() {
        if (_state.value.isLoading) return
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            createQuizResponse(_state.value.plants)
        }
    }

    fun onAnimationComplete() {
        _state.update { it.copy(nextQuiz = null) }
    }

    private fun createQuizResponse(plants: List<Plant>?) {
        getPlantResponseUseCase.invoke(plants, _state.value.nextQuiz).let { getPlantResponse ->
            if (getPlantResponse is DataState.Error) {
                fetchPlants()
            } else if (getPlantResponse is DataState.Success) {
                _state.update {
                    it.copy(
                        plants = plants,
                        quiz = getPlantResponse.data.first(),
                        nextQuiz = getPlantResponse.data.getOrNull(1),
                        isLoading = false
                    )
                }
                sendEvent(Event.NavigateToHomeScreen)
            }
        }
    }
}