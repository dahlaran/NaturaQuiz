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
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
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

    private val testPlants = listOf(
        Plant(1, "Rose", "Rosa rubiginosa", "https://example.com/rose.jpg", 1753, "Rosaceae", "Rosa"),
        Plant(2, "Tulip", "Tulipa gesneriana", "https://example.com/tulip.jpg", 1753, "Liliaceae", "Tulipa"),
        Plant(3, "Oak", "Quercus robur", "https://example.com/oak.jpg", 1753, "Fagaceae", "Quercus")
    )

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
    fun `initial state should be correct`() = runTest {
        viewModel.state.test {
            val initialState = awaitItem()
            assertFalse(initialState.isLoading)
            assertNull(initialState.plants)
            assertNull(initialState.quiz)
            assertNull(initialState.nextQuiz)
            assertNull(initialState.error)
            assertEquals(0, initialState.streak)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `fetchPlants success should update state with plants and quiz`() = runTest {
        val quiz = Quiz(testPlants[0], testPlants[1], true)
        val nextQuiz = Quiz(testPlants[1], testPlants[2], false)

        every { getPlantsUseCase.invoke() } returns flow {
            emit(DataState.Success(testPlants))
        }
        coEvery { getQuizResponseUseCase.invoke(any(), any()) } returns DataState.Success(
            listOf(quiz, nextQuiz)
        )

        viewModel.onEvent(QuizViewEvent.OnArriveOnQuizScreen)
        advanceUntilIdle()

        viewModel.state.test(timeout = 2.seconds) {
            val state = awaitItem()
            assertEquals(testPlants, state.plants)
            assertNotNull(state.quiz)
            assertNotNull(state.nextQuiz)
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
            assertNull(state.plants)
            assertNull(state.quiz)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `handleAnswer should update streak correctly for correct answer`() = runTest {
        val currentQuiz = Quiz(testPlants[0], testPlants[1], leftIsGoodAnswer = true)
        val nextQuiz = Quiz(testPlants[1], testPlants[2], leftIsGoodAnswer = false)

        every { getPlantsUseCase.invoke() } returns flow {
            emit(DataState.Success(testPlants))
        }
        coEvery { getQuizResponseUseCase.invoke(any(), any()) } returns DataState.Success(
            listOf(currentQuiz, nextQuiz)
        )

        viewModel.onEvent(QuizViewEvent.OnArriveOnQuizScreen)
        advanceUntilIdle()

        var initialStreak = 0
        var finalStreak = 0

        viewModel.state.test {
            val initialState = awaitItem()
            initialStreak = initialState.streak
            assertEquals(0, initialStreak)

            viewModel.onEvent(QuizViewEvent.HandelAnswer(isLeft = true))
            advanceUntilIdle()

            val newState = awaitItem()
            finalStreak = newState.streak
            assertTrue("Streak should increase for correct answer", finalStreak > initialStreak)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `handleAnswer should reset streak for incorrect answer`() = runTest {
        val currentQuiz = Quiz(testPlants[0], testPlants[1], leftIsGoodAnswer = true)
        val nextQuiz = Quiz(testPlants[1], testPlants[2], leftIsGoodAnswer = false)

        every { getPlantsUseCase.invoke() } returns flow {
            emit(DataState.Success(testPlants))
        }
        coEvery { getQuizResponseUseCase.invoke(any(), any()) } returns DataState.Success(
            listOf(currentQuiz, nextQuiz)
        )

        viewModel.onEvent(QuizViewEvent.OnArriveOnQuizScreen)
        advanceUntilIdle()


        val stateField = QuizViewModel::class.java.getDeclaredField("_state")
        stateField.isAccessible = true
        val stateFlow = stateField.get(viewModel) as MutableStateFlow<QuizState>
        stateFlow.value = stateFlow.value.copy(streak = 5)

        viewModel.state.test {
            val initialState = awaitItem()
            assertTrue("Initial streak should be 5", initialState.streak == 5)

            viewModel.onEvent(QuizViewEvent.HandelAnswer(isLeft = false))
            advanceUntilIdle()

            val newState = awaitItem()
            assertEquals("Streak should reset to 0 for incorrect answer", 0, newState.streak)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `quiz generation should progress to next quiz`() = runTest {
        val initialQuiz = Quiz(testPlants[0], testPlants[1], true)
        val initialNextQuiz = Quiz(testPlants[1], testPlants[2], false)

        every { getPlantsUseCase.invoke() } returns flow {
            emit(DataState.Success(testPlants))
        }
        coEvery { getQuizResponseUseCase.invoke(any(), any()) } returns DataState.Success(
            listOf(initialQuiz, initialNextQuiz)
        )

        viewModel.onEvent(QuizViewEvent.OnArriveOnQuizScreen)
        advanceUntilIdle()

        viewModel.state.test {
            val initialState = awaitItem()
            val initialQuizValue = initialState.quiz
            val initialNextQuizValue = initialState.nextQuiz

            assertNotNull("Initial quiz should not be null", initialQuizValue)
            assertNotNull("Initial next quiz should not be null", initialNextQuizValue)

            viewModel.onEvent(QuizViewEvent.HandelAnswer(true))
            advanceUntilIdle()

            val newState = awaitItem()
            assertNotNull("New quiz should not be null", newState.quiz)
            assertNotNull("New next quiz should not be null", newState.nextQuiz)

            val hasChanged = newState.quiz != initialQuizValue ||
                    newState.nextQuiz != initialNextQuizValue ||
                    newState.streak != initialState.streak

            assertTrue("State should change after answer", hasChanged)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `refresh should reset state and fetch new plants`() = runTest {
        val quiz = Quiz(testPlants[0], testPlants[1], true)
        val nextQuiz = Quiz(testPlants[1], testPlants[2], false)

        every { getPlantsUseCase.invoke() } returns flow {
            emit(DataState.Success(testPlants))
        }
        coEvery { getQuizResponseUseCase.invoke(any(), any()) } returns DataState.Success(
            listOf(quiz, nextQuiz)
        )

        viewModel.onEvent(QuizViewEvent.Refresh)
        advanceUntilIdle()

        verify { getPlantsUseCase.invoke() }

        viewModel.state.test(timeout = 2.seconds) {
            val state = awaitItem()
            assertNotNull("Plants should not be null after refresh", state.plants)
            assertNotNull("Quiz should not be null after refresh", state.quiz)
            assertNotNull("Next quiz should not be null after refresh", state.nextQuiz)
            assertFalse("Should not be loading after completion", state.isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loading state should be managed correctly`() = runTest {
        val quiz = Quiz(testPlants[0], testPlants[1], true)
        val nextQuiz = Quiz(testPlants[1], testPlants[2], false)

        every { getPlantsUseCase.invoke() } returns flow {
            // Simulate loading delay
            delay(100)
            emit(DataState.Success(testPlants))
        }

        coEvery { getQuizResponseUseCase.invoke(any(), any()) } returns DataState.Success(
            listOf(quiz, nextQuiz)
        )

        val loadingStates = mutableListOf<Boolean>()

        viewModel.state.test {
            loadingStates.add(awaitItem().isLoading)

            viewModel.onEvent(QuizViewEvent.OnArriveOnQuizScreen)
            loadingStates.add(awaitItem().isLoading)

            advanceUntilIdle()
            loadingStates.add(awaitItem().isLoading)

            cancelAndIgnoreRemainingEvents()
        }

        assertEquals("Should have 3 loading states", 3, loadingStates.size)
        assertFalse("Initial state should not be loading", loadingStates[0])
        assertTrue("Should be loading during fetch", loadingStates[1])
        assertFalse("Should not be loading after completion", loadingStates[2])
    }

    @Test
    fun `all event types should call getPlantsUseCase`() = runTest {
        val quiz = Quiz(testPlants[0], testPlants[1], true)
        val nextQuiz = Quiz(testPlants[1], testPlants[2], false)

        every { getPlantsUseCase.invoke() } returns flow {
            emit(DataState.Success(testPlants))
        }
        coEvery { getQuizResponseUseCase.invoke(any(), any()) } returns DataState.Success(
            listOf(quiz, nextQuiz)
        )

        viewModel.onEvent(QuizViewEvent.OnArriveOnSplash)
        advanceUntilIdle()

        viewModel.onEvent(QuizViewEvent.OnArriveOnQuizScreen)
        advanceUntilIdle()

        viewModel.onEvent(QuizViewEvent.Refresh)
        advanceUntilIdle()

        verify(atLeast = 3) { getPlantsUseCase.invoke() }
    }

    @Test
    fun `error in quiz generation should trigger plant refetch`() = runTest {
        every { getPlantsUseCase.invoke() } returns flow {
            emit(DataState.Success(testPlants))
        }

        coEvery { getQuizResponseUseCase.invoke(any(), any()) } returns DataState.Error(AppError.EmptyResultError) andThen
                DataState.Success(listOf(Quiz(testPlants[0], testPlants[1], true), Quiz(testPlants[1], testPlants[2], false)))

        viewModel.onEvent(QuizViewEvent.OnArriveOnQuizScreen)
        advanceUntilIdle()

        verify(atLeast = 2) { getPlantsUseCase.invoke() }
    }
}