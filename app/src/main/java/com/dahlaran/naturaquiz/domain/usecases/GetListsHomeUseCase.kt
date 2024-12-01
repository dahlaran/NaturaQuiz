package com.dahlaran.naturaquiz.domain.usecases

import com.dahlaran.naturaquiz.core.data.DataState
import com.dahlaran.naturaquiz.domain.PlantRepository
import com.dahlaran.naturaquiz.domain.entities.ListsHome
import com.dahlaran.naturaquiz.domain.entities.Plant
import javax.inject.Inject

class GetListsHomeUseCase @Inject constructor(private val plantRepository: PlantRepository) {
    suspend operator fun invoke(): DataState<ListsHome> {
        // Get lists that will be displayed on the home screen
        return plantRepository.getListsHome()
    }
}