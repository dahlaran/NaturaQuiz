package com.dahlaran.naturaquiz.data.model

import com.dahlaran.naturaquiz.domain.entities.Specie


/**
 * Model send by the API when fetching species
 */
data class SpeciesResponse(
    val data: List<Specie>
)