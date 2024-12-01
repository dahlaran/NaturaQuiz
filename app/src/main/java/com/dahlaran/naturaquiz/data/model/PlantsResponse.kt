package com.dahlaran.naturaquiz.data.model

import com.dahlaran.naturaquiz.domain.entities.Plant


/**
 * Model send by the API when fetching plants
 */
data class PlantsResponse(
    val data: List<Plant>
)