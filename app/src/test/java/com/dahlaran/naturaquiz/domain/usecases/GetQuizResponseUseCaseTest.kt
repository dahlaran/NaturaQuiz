package com.dahlaran.naturaquiz.domain.usecases

import com.dahlaran.naturaquiz.core.data.DataState
import com.dahlaran.naturaquiz.domain.entities.Plant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetQuizResponseUseCaseTest {
    private lateinit var useCase: GetQuizResponseUseCase
    private lateinit var testPlants: List<Plant>

    @Before
    fun setup() {
        useCase = GetQuizResponseUseCase()
        testPlants = listOf(
            Plant(1, "Plant1", "Scientific1", "url1", 2020, "Family1", "Genus1"),
            Plant(2, "Plant2", "Scientific2", "url2", 2021, "Family2", "Genus2"),
            Plant(3, "Plant3", "Scientific3", "url3", 2022, "Family3", "Genus3")
        )
    }

    @Test
    fun `invoke with valid plants should return success with two quizzes`() {
        val result = useCase.invoke(testPlants, null)

        assertTrue(result is DataState.Success)
        assertEquals(2, (result as DataState.Success).data.size)
    }

    @Test
    fun `invoke with empty plants should return error`() {
        val result = useCase.invoke(emptyList(), null)

        assertTrue(result is DataState.Error)
    }

    @Test
    fun `invoke with null plants should return error`() {
        val result = useCase.invoke(null, null)

        assertTrue(result is DataState.Error)
    }

    @Test
    fun `generated quizzes should have different plants`() {
        val result = useCase.invoke(testPlants, null)

        assertTrue(result is DataState.Success)
        val quizzes = (result as DataState.Success).data
        val quiz1 = quizzes[0]
        val quiz2 = quizzes[1]

        assertTrue(quiz1.goodAnswer.id != quiz1.wrongAnswer.id)
        assertTrue(quiz2.goodAnswer.id != quiz2.wrongAnswer.id)
    }
}