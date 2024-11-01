package com.dahlaran.naturaquiz.domain.usecases

import com.dahlaran.naturaquiz.core.data.DataState
import com.dahlaran.naturaquiz.core.data.ErrorCode
import com.dahlaran.naturaquiz.core.data.RepoError
import com.dahlaran.naturaquiz.core.extensions.firstOrNullFromIndex
import com.dahlaran.naturaquiz.data.model.Plant
import com.dahlaran.naturaquiz.data.model.SelectedQuiz
import javax.inject.Inject

class GetQuizResponseUseCase @Inject constructor() {

    operator fun invoke(
        plants: List<Plant>?,
        selectedQuiz: SelectedQuiz?
    ): DataState<SelectedQuiz?> {
        if (plants == null) {
            return DataState.Error(RepoError(ErrorCode.CODE_NOT_DISPLAY))
        }

        val solution = getGoodPlant(plants, selectedQuiz?.goodAnswer)
            ?: return DataState.Error(RepoError(ErrorCode.CODE_NOT_DISPLAY))
        val wrongPlant = getWrongPlant(plants, solution, selectedQuiz?.wrongAnswer)
            ?: return DataState.Error(RepoError(ErrorCode.CODE_NOT_DISPLAY))

        val leftIsGoodAnswer = (0..1).random() == 0
        return SelectedQuiz(solution, wrongPlant, leftIsGoodAnswer).let {
            DataState.Success(it)
        }
    }

    private fun getGoodPlant(plants: List<Plant>, currentPlant: Plant?): Plant? {
        val nextPlant = if (currentPlant == null) {
            plants.firstOrNull { it.isEligibleForQuiz() }
        } else {
            val index = plants.indexOf(currentPlant)
            if (index != -1 && plants.size > index) {
                plants.firstOrNullFromIndex(index + 1) { it.isEligibleForQuiz() }
            } else {
                null
            }
        }
        return nextPlant
    }

    private fun getWrongPlant(plants: List<Plant>, goodPlant: Plant, oldWrongAnswer: Plant?): Plant? {
        val wrongPlants =
            plants.filter { it.name != goodPlant.name && it.scientificName != goodPlant.scientificName && it.isEligibleForQuiz() && it.id != oldWrongAnswer?.id }
        val wrongPlant = wrongPlants.randomOrNull()
        return wrongPlant
    }
}