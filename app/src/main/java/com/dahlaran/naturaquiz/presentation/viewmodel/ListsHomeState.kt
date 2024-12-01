package com.dahlaran.naturaquiz.presentation.viewmodel

import com.dahlaran.naturaquiz.core.data.RepoError
import com.dahlaran.naturaquiz.domain.entities.Plant
import com.dahlaran.naturaquiz.domain.entities.Specie


data class ListsHomeState(
    val plants : List<Plant>? = null,
    val species : List<Specie>? = null,
    val error: RepoError? = null,
    val isLoading: Boolean = false,)