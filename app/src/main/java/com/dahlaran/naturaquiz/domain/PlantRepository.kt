package com.dahlaran.naturaquiz.domain

import com.dahlaran.naturaquiz.core.DataState
import com.dahlaran.naturaquiz.data.model.Plant

interface PlantRepository {

    var plantNumber: Int

    suspend fun getPlantsCount(): DataState<Int>

    suspend fun getPlants() : DataState<List<Plant>>
}