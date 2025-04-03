package com.dahlaran.naturaquiz.data

import com.dahlaran.naturaquiz.core.data.AppError
import com.dahlaran.naturaquiz.core.data.DataState
import com.dahlaran.naturaquiz.core.data.ErrorMapper
import com.dahlaran.naturaquiz.core.network.NetworkChecker
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
class PlantRepositoryImpl @Inject constructor(private val plantService: PlantService, private val networkChecker: NetworkChecker) :
    PlantRepository {
    companion object {
        const val PAGE_SIZE = 20
    }

    override var plantNumber = 0
    override suspend fun getPlantsCount(): DataState<Int> {
        return try {
            if (plantNumber == 0) {
                // Check for internet connection first
                if (!networkChecker.isConnected()) {
                    DataState.Error(AppError.NoInternetConnection)
                }
                val response = plantService.getPlantsCount()
                if (response.isSuccessful) {
                    plantNumber = response.body()?.count ?: 0
                    if (plantNumber == 0) {
                        DataState.Error(AppError.EmptyResultError)
                    } else {
                        DataState.Success(plantNumber)
                    }
                }
                DataState.Error(ErrorMapper.mapResponseToAppError(response))
            }
            DataState.Success(plantNumber)
        } catch (e: Exception) {
            DataState.Error(ErrorMapper.mapThrowableToAppError(e))
        }
    }

    override suspend fun getPlants(): DataState<List<Plant>> {
        return try {
            // Check for internet connection first
            if (!networkChecker.isConnected()) {
                return DataState.Error(AppError.NoInternetConnection)
            }

            val randomPage = (1..plantNumber / PAGE_SIZE).random()
            val response = plantService.getPlants(randomPage)

            if (response.isSuccessful) {
                val plants = response.body()?.data ?: emptyList()
                if (plants.isEmpty()) {
                    DataState.Error(AppError.EmptyResultError)
                } else {
                    DataState.Success(plants)
                }
            } else {
                DataState.Error(ErrorMapper.mapResponseToAppError(response))
            }
        } catch (e: Exception) {
            DataState.Error(ErrorMapper.mapThrowableToAppError(e))
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
                DataState.Error(ErrorMapper.mapResponseToAppError(if (!plantsResponse.isSuccessful) plantsResponse else speciesResponse))
            }
        } catch (e: Exception) {
            DataState.Error(ErrorMapper.mapThrowableToAppError(e))
        }
    }
}