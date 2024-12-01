package com.dahlaran.naturaquiz.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.dahlaran.naturaquiz.core.bus.Event
import com.dahlaran.naturaquiz.core.data.BaseViewModel
import com.dahlaran.naturaquiz.core.data.DataState
import com.dahlaran.naturaquiz.domain.entities.Plant
import com.dahlaran.naturaquiz.domain.usecases.GetPlantsUseCase
import com.dahlaran.naturaquiz.domain.usecases.GetQuizResponseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the quiz screen
 */
@HiltViewModel
class QuizViewModel @Inject constructor(
    private val getPlantsUseCase: GetPlantsUseCase,
    private val getPlantResponseUseCase: GetQuizResponseUseCase
) : BaseViewModel() {

    private val _state = MutableStateFlow(QuizState())
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

    /**
     * Handle the answer of the user
     *
     * @param isLeft : Boolean to know if the user clicked/dragged on the left button
     */
    fun handleAnswer(isLeft: Boolean) {
        // Check if the answer is correct
        val isCorrect = state.value.quiz?.let { quiz ->
            if (isLeft) quiz.leftIsGoodAnswer else !quiz.leftIsGoodAnswer
        } ?: false

        // TODO : Store the streak in the database to display the best score done by the user
        if (isCorrect) {
            _state.update { it.copy(streak = state.value.streak + 1) }
        }else {
            _state.update { it.copy(streak = 0) }
        }

        nextPlant()
    }

    private fun nextPlant() {
        if (_state.value.isLoading) return
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            createQuizResponse(_state.value.plants)
        }
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
                        isLoading = false,
                        error = null
                    )
                }
                sendEvent(Event.NavigateToHomeScreen)
            }
        }
    }
}