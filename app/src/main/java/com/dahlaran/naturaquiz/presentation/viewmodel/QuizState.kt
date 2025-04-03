package com.dahlaran.naturaquiz.presentation.viewmodel

import com.dahlaran.naturaquiz.core.data.AppError
import com.dahlaran.naturaquiz.domain.entities.Plant
import com.dahlaran.naturaquiz.domain.entities.Quiz

/**
 * PlantState is a data class that can be used to handle the state of a Quiz
 * It can be used to handle the state of a Quiz in a ViewModel
 *
 * @param isLoading : Loading state
 * @param plants : List of plants
 * @param quiz : Current quiz
 * @param nextQuiz : Next quiz (for more fluidity)
 * @param error : Error message
 * @param streak : Current streak of the user
 */
data class QuizState(
    val isLoading: Boolean = false,
    val plants: List<Plant>? = null,
    val quiz: Quiz? = null,
    val nextQuiz: Quiz? = null,
    val error: AppError? = null,
    val streak: Int = 0,
)