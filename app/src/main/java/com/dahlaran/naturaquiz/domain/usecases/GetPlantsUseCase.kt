package com.dahlaran.naturaquiz.domain.usecases


import com.dahlaran.naturaquiz.core.data.DataState
import com.dahlaran.naturaquiz.domain.PlantRepository
import com.dahlaran.naturaquiz.domain.entities.Plant
import javax.inject.Inject

class GetPlantsUseCase @Inject constructor(private val plantRepository: PlantRepository) {
    suspend operator fun invoke(): DataState<List<Plant>> {
        // If the plant number is not set, get it from the server
        if (plantRepository.plantNumber == 0) {
            val response = plantRepository.getPlantsCount()
            if (response is DataState.Error) {
                return response
            } else {
                // Set the plant number in the repository
                plantRepository.plantNumber = (response as DataState.Success).data
            }
        }
        // Get some plants from the server
        return plantRepository.getPlants()
    }
}