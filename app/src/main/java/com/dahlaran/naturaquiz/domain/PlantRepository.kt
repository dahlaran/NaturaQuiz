package com.dahlaran.naturaquiz.domain

import com.dahlaran.naturaquiz.core.data.DataState
import com.dahlaran.naturaquiz.domain.entities.ListsHome
import com.dahlaran.naturaquiz.domain.entities.Plant
import kotlinx.coroutines.flow.Flow

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
     * Fetch plants from the API
     */
    fun getPlants(): Flow<DataState<List<Plant>>>

    /**
     * Fetch list from the API
     */
    fun getListsHome(): Flow<DataState<ListsHome>>
}