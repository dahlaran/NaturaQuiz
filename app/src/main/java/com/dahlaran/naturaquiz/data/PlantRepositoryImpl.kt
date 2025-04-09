package com.dahlaran.naturaquiz.data

import com.dahlaran.naturaquiz.core.data.AppError
import com.dahlaran.naturaquiz.core.data.DataState
import com.dahlaran.naturaquiz.core.data.ErrorMapper
import com.dahlaran.naturaquiz.core.network.NetworkChecker
import com.dahlaran.naturaquiz.domain.PlantRepository
import com.dahlaran.naturaquiz.domain.entities.ListsHome
import com.dahlaran.naturaquiz.domain.entities.Plant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlantRepositoryImpl @Inject constructor(
    private val plantService: PlantService,
    private val networkChecker: NetworkChecker
) : PlantRepository {
    companion object {
        const val PAGE_SIZE = 20
    }

    override var plantNumber = 0

    override fun getPlants(): Flow<DataState<List<Plant>>> = flow {
        try {
            // Check for internet connection first
            if (!networkChecker.isConnected()) {
                emit(DataState.Error(AppError.NoInternetConnection))
                return@flow
            }

            // If the plant number is not set, get it from the server
            if (plantNumber == 0) {
                val plantsCountResponse = plantService.getPlantsCount()
                plantNumber = plantsCountResponse.body()?.count ?: 0
                if (plantNumber == 0) {
                    emit(DataState.Error(AppError.EmptyResultError))
                    return@flow
                }
            }

            val randomPage = (1..plantNumber / PAGE_SIZE).random()
            val response = plantService.getPlants(randomPage)

            if (response.isSuccessful) {
                val plants = response.body()?.data ?: emptyList()
                if (plants.isEmpty()) {
                    emit(DataState.Error(AppError.EmptyResultError))
                } else {
                    emit(DataState.Success(plants))
                }
            } else {
                emit(DataState.Error(ErrorMapper.mapResponseToAppError(response)))
            }
        } catch (e: Exception) {
            emit(DataState.Error(ErrorMapper.mapThrowableToAppError(e)))
        }
    }.flowOn(Dispatchers.IO)

    override fun getListsHome(): Flow<DataState<ListsHome>> = flow {
        try {
            if (!networkChecker.isConnected()) {
                emit(DataState.Error(AppError.NoInternetConnection))
                return@flow
            }

            // Parallel network requests
            val results = coroutineScope {
                val plantsDeferred = async { plantService.getPlants(2) }
                val speciesDeferred = async { plantService.getSpecies() }

                Pair(plantsDeferred.await(), speciesDeferred.await())
            }

            val (plantsResponse, speciesResponse) = results

            if (plantsResponse.isSuccessful && speciesResponse.isSuccessful) {
                val plants = plantsResponse.body()?.data ?: emptyList()
                val species = speciesResponse.body()?.data ?: emptyList()

                val listsHome = ListsHome(plants, species)
                emit(DataState.Success(listsHome))
            } else {
                val error = if (!plantsResponse.isSuccessful) {
                    ErrorMapper.mapResponseToAppError(plantsResponse)
                } else {
                    ErrorMapper.mapResponseToAppError(speciesResponse)
                }
                emit(DataState.Error(error))
            }
        } catch (e: Exception) {
            emit(DataState.Error(ErrorMapper.mapThrowableToAppError(e)))
        }
    }.flowOn(Dispatchers.IO)
}