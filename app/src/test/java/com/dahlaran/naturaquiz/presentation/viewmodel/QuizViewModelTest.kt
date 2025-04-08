package com.dahlaran.naturaquiz.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.dahlaran.naturaquiz.core.data.AppError
import com.dahlaran.naturaquiz.core.data.DataState
import com.dahlaran.naturaquiz.domain.entities.Plant
import com.dahlaran.naturaquiz.domain.entities.Quiz
import com.dahlaran.naturaquiz.domain.usecases.GetPlantsUseCase
import com.dahlaran.naturaquiz.domain.usecases.GetQuizResponseUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

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
        viewModel = QuizViewModel(getPlantsUseCase, getQuizResponseUseCase)
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

        every { getPlantsUseCase.invoke() } returns flow {
            emit(DataState.Success(plants))
        }
        coEvery { getQuizResponseUseCase.invoke(plants, null) } returns DataState.Success(
            listOf(
                quiz,
                nextQuiz
            )
        )

        viewModel.onEvent(QuizViewEvent.OnArriveOnQuizScreen)
        advanceUntilIdle()

        viewModel.state.test(timeout = 2.seconds) {
            val state = awaitItem()
            assertEquals(plants, state.plants)
            assertEquals(quiz, state.quiz)
            assertEquals(nextQuiz, state.nextQuiz)
            assertNull(state.error)
            assertFalse(state.isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `fetchPlants error should update state with error`() = runTest {
        val error = AppError.NetworkError()
        every { getPlantsUseCase.invoke() } returns flow {
            emit(DataState.Error(error))
        }

        viewModel.onEvent(QuizViewEvent.OnArriveOnQuizScreen)
        advanceUntilIdle()

        viewModel.state.test(timeout = 2.seconds) {
            val state = awaitItem()
            assertEquals(error, state.error)
            assertFalse(state.isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `handleAnswer correct should increment streak`() = runTest {
        val plants = listOf(
            Plant(1, "Plant1", "Scientific1", "url1", 2020, "Family1", "Genus1"),
            Plant(2, "Plant2", "Scientific2", "url2", 2021, "Family2", "Genus2")
        )
        val currentQuiz = Quiz(plants[0], plants[1], true)
        val nextQuiz = Quiz(plants[1], plants[0], false)
        val newQuiz = Quiz(plants[0], plants[1], false)

        every { getPlantsUseCase.invoke() } returns flow {
            emit(DataState.Success(plants))
        }
        coEvery { getQuizResponseUseCase.invoke(plants, null) } returns DataState.Success(
            listOf(
                currentQuiz,
                nextQuiz
            )
        )

        viewModel.onEvent(QuizViewEvent.OnArriveOnQuizScreen)
        advanceUntilIdle()

        coEvery {
            getQuizResponseUseCase.invoke(
                plants,
                nextQuiz
            )
        } returns DataState.Success(listOf(newQuiz))

        viewModel.state.test {
            val initialState = awaitItem()

            assertEquals(currentQuiz, initialState.quiz)
            assertEquals(0, initialState.streak)

            viewModel.onEvent(QuizViewEvent.HandelAnswer(isLeft = initialState.quiz!!.leftIsGoodAnswer))

            advanceUntilIdle()

            val newState = awaitItem()
            assertEquals(1, newState.streak)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `handleAnswer incorrect should reset streak`() = runTest {
        val plants = listOf(
            Plant(1, "Plant1", "Scientific1", "url1", 2020, "Family1", "Genus1"),
            Plant(2, "Plant2", "Scientific2", "url2", 2021, "Family2", "Genus2")
        )
        val currentQuiz = Quiz(plants[0], plants[1], true)
        val nextQuiz = Quiz(plants[1], plants[0], false)
        val newQuiz = Quiz(plants[0], plants[1], false)

        every { getPlantsUseCase.invoke() } returns flow {
            emit(DataState.Success(plants))
        }
        coEvery { getQuizResponseUseCase.invoke(plants, null) } returns DataState.Success(
            listOf(
                currentQuiz,
                nextQuiz
            )
        )

        viewModel.onEvent(QuizViewEvent.OnArriveOnQuizScreen)
        advanceUntilIdle()

        val fieldStreak = QuizViewModel::class.java.getDeclaredField("_state")
        fieldStreak.isAccessible = true
        val stateFlow = fieldStreak.get(viewModel) as MutableStateFlow<QuizState>
        stateFlow.value = stateFlow.value.copy(streak = 3)

        coEvery {
            getQuizResponseUseCase.invoke(
                plants,
                nextQuiz
            )
        } returns DataState.Success(listOf(newQuiz))

        viewModel.state.test {
            val initialState = awaitItem()

            assertEquals(3, initialState.streak)
            viewModel.onEvent(QuizViewEvent.HandelAnswer(isLeft = !initialState.quiz!!.leftIsGoodAnswer))
            advanceUntilIdle()

            val newState = awaitItem()
            assertEquals(0, newState.streak)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onEvent Refresh should fetch plants and create quiz`() = runTest {
        val plants = listOf(
            Plant(1, "Plant1", "Scientific1", "url1", 2020, "Family1", "Genus1"),
            Plant(2, "Plant2", "Scientific2", "url2", 2021, "Family2", "Genus2")
        )
        val quiz = Quiz(plants[0], plants[1], true)
        val nextQuiz = Quiz(plants[1], plants[0], false)

        every { getPlantsUseCase.invoke() } returns flow {
            emit(DataState.Success(plants))
        }
        coEvery { getQuizResponseUseCase.invoke(plants, null) } returns DataState.Success(
            listOf(
                quiz,
                nextQuiz
            )
        )

        viewModel.onEvent(QuizViewEvent.Refresh)
        advanceUntilIdle()

        viewModel.state.test(timeout = 2.seconds) {
            val state = awaitItem()
            assertEquals(plants, state.plants)
            assertEquals(quiz, state.quiz)
            assertEquals(nextQuiz, state.nextQuiz)
            assertNull(state.error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `nextPlant should update quiz and nextQuiz`() = runTest {
        val plants = listOf(
            Plant(1, "Plant1", "Scientific1", "url1", 2020, "Family1", "Genus1"),
            Plant(2, "Plant2", "Scientific2", "url2", 2021, "Family2", "Genus2")
        )
        val initialQuiz = Quiz(plants[0], plants[1], true)
        val initialNextQuiz = Quiz(plants[1], plants[0], false)
        val newNextQuiz = Quiz(plants[0], plants[1], true)

        every { getPlantsUseCase.invoke() } returns flow {
            emit(DataState.Success(plants))
        }
        coEvery { getQuizResponseUseCase.invoke(plants, null) } returns DataState.Success(
            listOf(initialQuiz, initialNextQuiz)
        )

        viewModel.onEvent(QuizViewEvent.OnArriveOnQuizScreen)
        advanceUntilIdle()

        coEvery {
            getQuizResponseUseCase.invoke(
                plants,
                initialNextQuiz
            )
        } returns DataState.Success(
            listOf(initialNextQuiz, newNextQuiz)
        )

        val states = mutableListOf<QuizState>()
        viewModel.state.test {
            states.add(awaitItem())

            viewModel.onEvent(QuizViewEvent.HandelAnswer(true))
            advanceUntilIdle()

            states.add(awaitItem())

            cancelAndIgnoreRemainingEvents()
        }

        assertEquals(initialQuiz, states[0].quiz)
        assertEquals(initialNextQuiz, states[0].nextQuiz)

        assertEquals(initialNextQuiz, states[1].quiz)
        assertEquals(newNextQuiz, states[1].nextQuiz)
    }
}