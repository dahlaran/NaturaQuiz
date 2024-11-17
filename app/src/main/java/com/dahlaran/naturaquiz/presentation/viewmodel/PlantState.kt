package com.dahlaran.naturaquiz.presentation.viewmodel

import com.dahlaran.naturaquiz.core.data.RepoError
import com.dahlaran.naturaquiz.domain.entities.Plant
import com.dahlaran.naturaquiz.domain.entities.Quiz

/**
 * PlantState is a data class that can be used to handle the state of a Plant
 * It can be used to handle the state of a Plant in a ViewModel
 */
data class PlantState(
    val plants : List<Plant>? = null,
    val quiz: Quiz? = null,
    val nextQuiz: Quiz? = null,
    val error: RepoError? = null,
    val isLoading: Boolean = false,
    val score: Int = 0,)