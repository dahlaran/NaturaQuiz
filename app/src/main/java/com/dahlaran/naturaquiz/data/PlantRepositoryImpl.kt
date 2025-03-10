package com.dahlaran.naturaquiz.data

import com.dahlaran.naturaquiz.core.data.DataState
import com.dahlaran.naturaquiz.core.data.RepoError
import com.dahlaran.naturaquiz.domain.PlantRepository
import com.dahlaran.naturaquiz.domain.entities.ListsHome
import com.dahlaran.naturaquiz.domain.entities.Plant
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
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


    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getListsHome(): DataState<ListsHome> = coroutineScope {
        try {
            val plantsResponseAsync = async { plantService.getPlants(2) }
            val speciesResponseAsync = async { plantService.getSpecies() }

            awaitAll(plantsResponseAsync, speciesResponseAsync)
            val plantsResponse = plantsResponseAsync.getCompleted()
            val speciesResponse = speciesResponseAsync.getCompleted()

            if (plantsResponse.isSuccessful && speciesResponse.isSuccessful) {
                DataState.Success(
                    ListsHome(
                        plantsResponse.body()?.data ?: emptyList(),
                        speciesResponse.body()?.data ?: emptyList()
                    )
                )
            } else {
                DataState.Error(
                    RepoError.fromResponseBody(
                        plantsResponse.errorBody() ?: speciesResponse.errorBody()
                    )
                )
            }
        } catch (e: Exception) {
            DataState.Error(RepoError.fromException(e))
        }
    }
}