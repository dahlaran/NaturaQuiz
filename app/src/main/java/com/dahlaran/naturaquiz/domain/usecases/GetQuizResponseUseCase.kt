package com.dahlaran.naturaquiz.domain.usecases

import com.dahlaran.naturaquiz.core.data.DataState
import com.dahlaran.naturaquiz.core.data.ErrorCode
import com.dahlaran.naturaquiz.core.data.RepoError
import com.dahlaran.naturaquiz.core.extensions.firstOrNullFromIndex
import com.dahlaran.naturaquiz.domain.entities.Plant
import com.dahlaran.naturaquiz.domain.entities.Quiz
import javax.inject.Inject

class GetQuizResponseUseCase @Inject constructor() {

    operator fun invoke(
        plants: List<Plant>?,
        displayedQuiz: Quiz?,
    ): DataState<List<Quiz>> {
        if (plants == null) {
            return DataState.Error(RepoError(ErrorCode.CODE_NOT_DISPLAY))
        }
        val generatedQuiz = mutableListOf<Quiz>()

        val displayedGeneratedQuiz: Quiz? = displayedQuiz ?: generateQuiz(plants, null)
        displayedGeneratedQuiz?.let { displayed ->
            generatedQuiz.add(displayed)
            val newQuiz = generateQuiz(plants, displayed)
            newQuiz?.let { new ->
                generatedQuiz.add(new)
            } ?: return DataState.Error(RepoError(ErrorCode.CODE_NOT_DISPLAY))
        } ?: return DataState.Error(RepoError(ErrorCode.CODE_NOT_DISPLAY))

        return DataState.Success(generatedQuiz)
    }

    private fun generateQuiz(plants: List<Plant>, quiz: Quiz?): Quiz? {
        val goodPlant = getGoodPlant(plants, quiz?.goodAnswer)
            ?: return null
        val wrongPlant = getWrongPlant(plants, goodPlant, quiz?.wrongAnswer)
            ?: return null

        val leftIsGoodAnswer = (0..1).random() == 0
        return Quiz(goodPlant, wrongPlant, leftIsGoodAnswer)
    }

    private fun getGoodPlant(plants: List<Plant>, currentPlant: Plant?): Plant? {
        val nextPlant = if (currentPlant == null) {
            plants.firstOrNull { it.isEligibleForQuiz() }
        } else {
            // Check if the current plant is still in the list
            val index = plants.indexOf(currentPlant)
            if (index != -1 && plants.size > index) {
                // Take next eligible plant that is after the current plant
                plants.firstOrNullFromIndex(index + 1) { it.isEligibleForQuiz() }
            } else {
                // Take first eligible plant
                plants.firstOrNull { it.isEligibleForQuiz() }
            }
        }
        return nextPlant
    }

    private fun getWrongPlant(plants: List<Plant>, goodPlant: Plant, oldWrongAnswer: Plant?): Plant? {
        // TODO: Improve logic to avoid getting the same wrong answer multiple times and avoid getting the good answer as a wrong answer for the next question
        val wrongPlants =
            plants.filter { (it.name != goodPlant.name || (it.name == null && it.scientificName != goodPlant.scientificName)) && it.isEligibleForWrongAnswer() && it.id != oldWrongAnswer?.id }
        val wrongPlant = wrongPlants.randomOrNull()
        return wrongPlant
    }
}