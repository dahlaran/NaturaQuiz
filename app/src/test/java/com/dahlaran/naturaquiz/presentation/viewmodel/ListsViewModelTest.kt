package com.dahlaran.naturaquiz.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.dahlaran.naturaquiz.core.data.AppError.NetworkError
import com.dahlaran.naturaquiz.core.data.DataState
import com.dahlaran.naturaquiz.domain.entities.ListsHome
import com.dahlaran.naturaquiz.domain.entities.Plant
import com.dahlaran.naturaquiz.domain.entities.Specie
import com.dahlaran.naturaquiz.domain.usecases.GetListsHomeUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class ListsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ListsViewModel
    private lateinit var getListsHomeUseCase: GetListsHomeUseCase
    private val testDispatcher = StandardTestDispatcher()

    private val testPlants = listOf(
        Plant(1, "Plant1", "Scientific1", "url1", 2020, "Family1", "Genus1")
    )
    private val testSpecies = listOf(
        Specie(1, "Species1", "SciSpecies1", "url1", 2020, "Family1", "Genus1")
    )
    private val testListsHome = ListsHome(testPlants, testSpecies)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getListsHomeUseCase = mockk()
        viewModel = ListsViewModel(getListsHomeUseCase)
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
            assertNull(initialState.species)
            assertNull(initialState.error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onEvent OnArriveOnList sets loading state and fetches lists`() = runTest {
        every { getListsHomeUseCase.invoke() } returns flow {
            emit(DataState.Success(testListsHome))
        }

        viewModel.onEvent(ListsViewEvent.OnArriveOnList)
        advanceUntilIdle()

        viewModel.state.test(timeout = 2.seconds) {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(testPlants, state.plants)
            assertEquals(testSpecies, state.species)
            assertNull(state.error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onEvent OnArriveOnSplash sets loading state and fetches lists`() = runTest {
        every { getListsHomeUseCase.invoke() } returns flow {
            emit(DataState.Success(testListsHome))
        }

        viewModel.onEvent(ListsViewEvent.OnArriveOnSplash)
        advanceUntilIdle()

        viewModel.state.test(timeout = 2.seconds) {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(testPlants, state.plants)
            assertEquals(testSpecies, state.species)
            assertNull(state.error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onEvent Refresh sets loading state and fetches lists`() = runTest {
        every { getListsHomeUseCase.invoke() } returns flow {
            emit(DataState.Success(testListsHome))
        }

        viewModel.onEvent(ListsViewEvent.Refresh)
        advanceUntilIdle()

        viewModel.state.test(timeout = 2.seconds) {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(testPlants, state.plants)
            assertEquals(testSpecies, state.species)
            assertNull(state.error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `fetchLists updates loading state correctly`() = runTest {
        every { getListsHomeUseCase.invoke() } returns flow {
            kotlinx.coroutines.delay(100)
            emit(DataState.Success(testListsHome))
        }

        val loadingStates = mutableListOf<Boolean>()

        viewModel.state.test {
            loadingStates.add(awaitItem().isLoading)

            viewModel.onEvent(ListsViewEvent.Refresh)
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
    fun `fetchLists error updates state with error`() = runTest {
        val error = NetworkError()
        every { getListsHomeUseCase.invoke() } returns flow {
            emit(DataState.Error(error))
        }

        viewModel.onEvent(ListsViewEvent.Refresh)
        advanceUntilIdle()

        viewModel.state.test(timeout = 2.seconds) {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(error, state.error)
            assertNull(state.plants)
            assertNull(state.species)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `fetchLists success replaces previous data`() = runTest {
        val initialLists = ListsHome(
            listOf(Plant(1, "Initial", "InitialScientific", "url1", 2020, "Family1", "Genus1")),
            listOf(Specie(1, "InitialSpecies", "InitialSci", "url1", 2020, "Family1", "Genus1"))
        )

        val updatedLists = ListsHome(
            listOf(
                Plant(2, "Updated", "UpdatedScientific", "url2", 2021, "Family2", "Genus2"),
                Plant(3, "Another", "AnotherScientific", "url3", 2022, "Family3", "Genus3")
            ),
            listOf(Specie(2, "UpdatedSpecies", "UpdatedSci", "url2", 2021, "Family2", "Genus2"))
        )

        every { getListsHomeUseCase.invoke() } returns flow {
            emit(DataState.Success(initialLists))
        }

        viewModel.onEvent(ListsViewEvent.Refresh)
        advanceUntilIdle()

        every { getListsHomeUseCase.invoke() } returns flow {
            emit(DataState.Success(updatedLists))
        }

        viewModel.onEvent(ListsViewEvent.Refresh)
        advanceUntilIdle()

        viewModel.state.test(timeout = 2.seconds) {
            val finalState = awaitItem()
            assertFalse(finalState.isLoading)
            assertEquals(updatedLists.plants, finalState.plants)
            assertEquals(updatedLists.species, finalState.species)
            assertEquals(2, finalState.plants?.size)
            assertEquals("Updated", finalState.plants?.get(0)?.name)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `all events call getListsHomeUseCase`() = runTest {
        every { getListsHomeUseCase.invoke() } returns flow {
            emit(DataState.Success(testListsHome))
        }

        viewModel.onEvent(ListsViewEvent.OnArriveOnList)
        advanceUntilIdle()

        viewModel.onEvent(ListsViewEvent.OnArriveOnSplash)
        advanceUntilIdle()

        viewModel.onEvent(ListsViewEvent.Refresh)
        advanceUntilIdle()

        verify(exactly = 3) { getListsHomeUseCase.invoke() }
    }

    @Test
    fun `error state is cleared on successful fetch`() = runTest {
        val error = NetworkError()

        every { getListsHomeUseCase.invoke() } returns flow {
            emit(DataState.Error(error))
        }

        viewModel.onEvent(ListsViewEvent.Refresh)
        advanceUntilIdle()

        viewModel.state.test {
            val errorState = awaitItem()
            assertEquals(error, errorState.error)
            assertNull(errorState.plants)
            cancelAndIgnoreRemainingEvents()
        }

        every { getListsHomeUseCase.invoke() } returns flow {
            emit(DataState.Success(testListsHome))
        }

        viewModel.onEvent(ListsViewEvent.Refresh)
        advanceUntilIdle()

        viewModel.state.test {
            val successState = awaitItem()
            assertNull(successState.error)
            assertEquals(testPlants, successState.plants)
            assertEquals(testSpecies, successState.species)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `state maintains consistency during multiple events`() = runTest {
        every { getListsHomeUseCase.invoke() } returns flow {
            emit(DataState.Success(testListsHome))
        }

        viewModel.onEvent(ListsViewEvent.Refresh)
        viewModel.onEvent(ListsViewEvent.OnArriveOnList)
        viewModel.onEvent(ListsViewEvent.OnArriveOnSplash)

        advanceUntilIdle()

        viewModel.state.test {
            val finalState = awaitItem()
            assertFalse(finalState.isLoading)
            assertEquals(testPlants, finalState.plants)
            assertEquals(testSpecies, finalState.species)
            assertNull(finalState.error)
            cancelAndIgnoreRemainingEvents()
        }

        verify(exactly = 3) { getListsHomeUseCase.invoke() }
    }
}