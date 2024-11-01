package com.dahlaran.naturaquiz.presentation.viewmodel

import com.dahlaran.naturaquiz.core.data.RepoError
import com.dahlaran.naturaquiz.data.model.Plant
import com.dahlaran.naturaquiz.data.model.SelectedQuiz

data class PlantState(
    val plants : List<Plant>? = null,
    val selectedQuiz: SelectedQuiz? = null,
    val error: RepoError? = null,
    val isLoading: Boolean = false)