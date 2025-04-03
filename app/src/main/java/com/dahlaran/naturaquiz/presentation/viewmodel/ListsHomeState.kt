package com.dahlaran.naturaquiz.presentation.viewmodel

import com.dahlaran.naturaquiz.core.data.AppError
import com.dahlaran.naturaquiz.domain.entities.Plant
import com.dahlaran.naturaquiz.domain.entities.Specie

/**
 * State for the Home screen lists
 *
 * @param plants : List of plants
 * @param species : List of species
 * @param error : Error message
 * @param isLoading : Loading state
 */
data class ListsHomeState(
    val plants: List<Plant>? = null,
    val species: List<Specie>? = null,
    val error: AppError? = null,
    val isLoading: Boolean = false,
)