package com.dahlaran.naturaquiz.domain.usecases

import com.dahlaran.naturaquiz.core.data.DataState
import com.dahlaran.naturaquiz.domain.PlantRepository
import com.dahlaran.naturaquiz.domain.entities.ListsHome
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetListsHomeUseCase @Inject constructor(private val plantRepository: PlantRepository) {
    operator fun invoke(): Flow<DataState<ListsHome>> {
        // Get lists that will be displayed on the home screen
        return plantRepository.getListsHome()
    }
}