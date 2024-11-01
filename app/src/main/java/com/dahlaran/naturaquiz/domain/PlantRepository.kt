package com.dahlaran.naturaquiz.domain

import com.dahlaran.naturaquiz.core.data.DataState
import com.dahlaran.naturaquiz.domain.entities.Plant

/**
 * PlantRepository is an interface that can be used to handle the state of a Plant
 * It can be used to handle the state of a Plant in a ViewModel
 */
interface PlantRepository {

    /**
     * Number of plants in the API
     */
    var plantNumber: Int

    /**
     * Fetch plants number from the API to set the plantNumber
     */
    suspend fun getPlantsCount(): DataState<Int>

    /**
     * Fetch plants from the API
     */
    suspend fun getPlants() : DataState<List<Plant>>
}