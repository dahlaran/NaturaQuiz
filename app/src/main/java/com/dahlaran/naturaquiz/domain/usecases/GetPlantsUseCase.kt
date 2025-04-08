package com.dahlaran.naturaquiz.domain.usecases


import com.dahlaran.naturaquiz.core.data.DataState
import com.dahlaran.naturaquiz.domain.PlantRepository
import com.dahlaran.naturaquiz.domain.entities.Plant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class GetPlantsUseCase @Inject constructor(private val plantRepository: PlantRepository) {
    operator fun invoke(): Flow<DataState<List<Plant>>> {
        return plantRepository.getPlants()
    }
}