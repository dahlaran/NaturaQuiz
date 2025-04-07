package com.dahlaran.naturaquiz.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dahlaran.naturaquiz.core.data.AppError
import com.dahlaran.naturaquiz.core.data.DataState
import com.dahlaran.naturaquiz.domain.entities.Plant
import com.dahlaran.naturaquiz.domain.entities.Quiz
import com.dahlaran.naturaquiz.domain.usecases.GetPlantsUseCase
import com.dahlaran.naturaquiz.domain.usecases.GetQuizResponseUseCase
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QuizViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: QuizViewModel
    private lateinit var getPlantsUseCase: GetPlantsUseCase
    private lateinit var getQuizResponseUseCase: GetQuizResponseUseCase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getPlantsUseCase = mockk()
        getQuizResponseUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchPlants success should update state with plants and quiz`() = runTest {
        val plants = listOf(
            Plant(1, "Plant1", "Scientific1", "url1", 2020, "Family1", "Genus1"),
            Plant(2, "Plant2", "Scientific2", "url2", 2021, "Family2", "Genus2")
        )
        val quiz = Quiz(plants[0], plants[1], true)
        val nextQuiz = Quiz(plants[1], plants[0], false)

        coEvery { getPlantsUseCase.invoke() } returns DataState.Success(plants)
        coEvery { getQuizResponseUseCase.invoke(plants, null) } returns DataState.Success(listOf(quiz, nextQuiz))

        viewModel = QuizViewModel(getPlantsUseCase, getQuizResponseUseCase)
        viewModel.onEvent(QuizViewEvent.OnArriveOnQuizScreen)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(plants, viewModel.state.value.plants)
        assertEquals(quiz, viewModel.state.value.quiz)
        assertEquals(nextQuiz, viewModel.state.value.nextQuiz)
        assertNull(viewModel.state.value.error)
        assertEquals(false, viewModel.state.value.isLoading)
    }

    @Test
    fun `fetchPlants error should update state with error`() = runTest {
        val error = AppError.NetworkError()
        coEvery { getPlantsUseCase.invoke() } returns DataState.Error(error)

        viewModel = QuizViewModel(getPlantsUseCase, getQuizResponseUseCase)

        viewModel.onEvent(QuizViewEvent.OnArriveOnQuizScreen)
        testDispatcher.scheduler.advanceUntilIdle()


        assertEquals(error, viewModel.state.value.error)
        assertEquals(false, viewModel.state.value.isLoading)
    }

    @Test
    fun `handleAnswer correct should increment streak`() {
        val plants = listOf(
            Plant(1, "Plant1", "Scientific1", "url1", 2020, "Family1", "Genus1"),
            Plant(2, "Plant2", "Scientific2", "url2", 2021, "Family2", "Genus2")
        )
        val currentQuiz = Quiz(plants[0], plants[1], true)
        val nextQuiz = Quiz(plants[1], plants[0], false)

        viewModel = spyk(
            QuizViewModel(getPlantsUseCase, getQuizResponseUseCase),
            recordPrivateCalls = true
        )

        coEvery { getPlantsUseCase.invoke() } returns DataState.Success(plants)
        coEvery { getQuizResponseUseCase.invoke(any(), any()) } returns DataState.Success(listOf(currentQuiz, nextQuiz))

        viewModel.onEvent(QuizViewEvent.OnArriveOnQuizScreen)
        testDispatcher.scheduler.advanceUntilIdle()

        coEvery { getQuizResponseUseCase.invoke(any(), any()) } returns DataState.Success(listOf(nextQuiz))
        viewModel.onEvent(QuizViewEvent.HandelAnswer(isLeft = viewModel.state.value.quiz!!.leftIsGoodAnswer) )
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, viewModel.state.value.streak)
    }

    @Test
    fun `handleAnswer incorrect should reset streak`() = runTest {
        val plants = listOf(
            Plant(1, "Plant1", "Scientific1", "url1", 2020, "Family1", "Genus1"),
            Plant(2, "Plant2", "Scientific2", "url2", 2021, "Family2", "Genus2")
        )
        val currentQuiz = Quiz(plants[0], plants[1], true)
        val nextQuiz = Quiz(plants[1], plants[0], false)

        viewModel = spyk(
            QuizViewModel(getPlantsUseCase, getQuizResponseUseCase),
            recordPrivateCalls = true
        )

        coEvery { getPlantsUseCase.invoke() } returns DataState.Success(plants)
        coEvery { getQuizResponseUseCase.invoke(any(), any()) } returns DataState.Success(listOf(currentQuiz, nextQuiz))

        viewModel.onEvent(QuizViewEvent.OnArriveOnQuizScreen)
        testDispatcher.scheduler.advanceUntilIdle()

        // Set initial streak through multiple correct answers
        repeat(3) {
            coEvery { getQuizResponseUseCase.invoke(any(), any()) } returns DataState.Success(listOf(nextQuiz))
            viewModel.onEvent(QuizViewEvent.HandelAnswer(isLeft = viewModel.state.value.quiz!!.leftIsGoodAnswer))
            testDispatcher.scheduler.advanceUntilIdle()
        }

        // Fail intentionally
        viewModel.onEvent(QuizViewEvent.HandelAnswer(isLeft =!viewModel.state.value.quiz!!.leftIsGoodAnswer))
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(0, viewModel.state.value.streak)
    }
}