package com.dahlaran.naturaquiz.domain.viewmodel

import com.dahlaran.naturaquiz.data.model.Plant

data class PlantState(
    val plants : List<Plant>? = null,
    val plantSelected: Plant? = null,
    val isLoading: Boolean = false)