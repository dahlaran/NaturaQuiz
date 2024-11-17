package com.dahlaran.naturaquiz.data

import com.dahlaran.naturaquiz.core.data.DataState
import com.dahlaran.naturaquiz.core.data.RepoError
import com.dahlaran.naturaquiz.domain.entities.Plant
import com.dahlaran.naturaquiz.domain.PlantRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlantRepositoryImpl @Inject constructor(private val plantService: PlantService) :
    PlantRepository {
        companion object {
            const val PAGE_SIZE = 20
        }

    override var plantNumber = 0
    override suspend fun getPlantsCount(): DataState<Int> {
        try {
            if (plantNumber == 0) {
                val response = plantService.getPlantsCount()
                if (response.isSuccessful) {
                    plantNumber = response.body()?.count ?: 0
                    return DataState.Success(plantNumber)
                }
                return DataState.Error(RepoError.fromResponseBody(response.errorBody()))
            }
            return DataState.Success(plantNumber)
        } catch (e: Exception) {
            return DataState.Error(RepoError.fromException(e))
        }
    }


    override suspend fun getPlants(): DataState<List<Plant>> {
        try {
            // Random page to get a different set of plants each time
            val randomPage = (1..plantNumber / PAGE_SIZE).random()

            val response = plantService.getPlants(randomPage)
            if (response.isSuccessful) {
                return DataState.Success(response.body()?.data ?: emptyList())
            }
            return DataState.Error(RepoError.fromResponseBody(response.errorBody()))
        } catch (e: Exception) {
            return DataState.Error(RepoError.fromException(e))
        }
    }
}