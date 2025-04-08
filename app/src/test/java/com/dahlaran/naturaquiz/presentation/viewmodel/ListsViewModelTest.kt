package com.dahlaran.naturaquiz.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.dahlaran.naturaquiz.core.data.AppError
import com.dahlaran.naturaquiz.core.data.DataState
import com.dahlaran.naturaquiz.domain.entities.ListsHome
import com.dahlaran.naturaquiz.domain.entities.Plant
import com.dahlaran.naturaquiz.domain.entities.Specie
import com.dahlaran.naturaquiz.domain.usecases.GetListsHomeUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
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
    fun `onEvent OnArriveOnList sets loading state and fetches lists`() = runTest {
        val plants = listOf(
            Plant(1, "Plant1", "Scientific1", "url1", 2020, "Family1", "Genus1")
        )
        val species = listOf(
            Specie(1, "Species1", "SciSpecies1", "url1", 2020, "Family1", "Genus1")
        )
        val listsHome = ListsHome(plants, species)

        every { getListsHomeUseCase.invoke() } returns flow {
            emit(DataState.Success(listsHome))
        }

        viewModel.onEvent(ListsViewEvent.OnArriveOnList)
        advanceUntilIdle()

        viewModel.state.test(timeout = 2.seconds) {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(plants, state.plants)
            assertEquals(species, state.species)
            assertNull(state.error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onEvent OnArriveOnSplash sets loading state and fetches lists`() = runTest {
        val plants = listOf(
            Plant(1, "Plant1", "Scientific1", "url1", 2020, "Family1", "Genus1")
        )
        val species = listOf(
            Specie(1, "Species1", "SciSpecies1", "url1", 2020, "Family1", "Genus1")
        )
        val listsHome = ListsHome(plants, species)

        every { getListsHomeUseCase.invoke() } returns flow {
            emit(DataState.Success(listsHome))
        }

        viewModel.onEvent(ListsViewEvent.OnArriveOnSplash)
        advanceUntilIdle()

        viewModel.state.test(timeout = 2.seconds) {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(plants, state.plants)
            assertEquals(species, state.species)
            assertNull(state.error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onEvent Refresh sets loading state and fetches lists`() = runTest {
        val plants = listOf(
            Plant(1, "Plant1", "Scientific1", "url1", 2020, "Family1", "Genus1")
        )
        val species = listOf(
            Specie(1, "Species1", "SciSpecies1", "url1", 2020, "Family1", "Genus1")
        )
        val listsHome = ListsHome(plants, species)

        every { getListsHomeUseCase.invoke() } returns flow {
            emit(DataState.Success(listsHome))
        }

        viewModel.onEvent(ListsViewEvent.Refresh)
        advanceUntilIdle()

        viewModel.state.test(timeout = 2.seconds) {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(plants, state.plants)
            assertEquals(species, state.species)
            assertNull(state.error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `fetchLists updates loading state correctly`() = runTest {
        val flowToReturn: Flow<DataState<ListsHome>> = flow {
            delay(100)
            emit(DataState.Success(ListsHome(emptyList(), emptyList())))
        }

        every { getListsHomeUseCase.invoke() } returns flowToReturn

        var loadingState: Boolean? = null
        var finalLoadingState: Boolean? = null

        viewModel.state.test {
            val initialState = awaitItem()
            assertFalse(initialState.isLoading)

            viewModel.onEvent(ListsViewEvent.Refresh)

            val loadingStateItem = awaitItem()
            loadingState = loadingStateItem.isLoading

            val finalStateItem = awaitItem()
            finalLoadingState = finalStateItem.isLoading

            cancelAndIgnoreRemainingEvents()
        }

        advanceUntilIdle()

        assertTrue(loadingState!!)
        assertFalse(finalLoadingState!!)
    }

    @Test
    fun `fetchLists error updates state with error`() = runTest {
        val error = AppError.NetworkError()
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
    fun `fetchLists handles multiple emissions`() = runTest {
        val firstPlants =
            listOf(Plant(1, "First", "FirstScientific", "url1", 2020, "Family1", "Genus1"))
        val secondPlants = listOf(
            Plant(2, "Updated", "UpdatedScientific", "url2", 2021, "Family2", "Genus2"),
            Plant(3, "Another", "AnotherScientific", "url3", 2022, "Family3", "Genus3")
        )

        every { getListsHomeUseCase.invoke() } returns flow {
            emit(DataState.Success(ListsHome(firstPlants, emptyList())))
            delay(200)
            emit(DataState.Success(ListsHome(secondPlants, emptyList())))
        }

        viewModel.onEvent(ListsViewEvent.Refresh)

        val states = mutableListOf<ListsHomeState>()
        viewModel.state.test {
            states.add(awaitItem())
            states.add(awaitItem())
            states.add(awaitItem())

            advanceUntilIdle()

            states.add(awaitItem())

            cancelAndIgnoreRemainingEvents()
        }

        assertEquals(4, states.size)

        assertFalse(states[0].isLoading)
        assertNull(states[0].plants)

        assertTrue(states[1].isLoading)

        assertFalse(states[2].isLoading)
        assertEquals(firstPlants, states[2].plants)

        assertFalse(states[3].isLoading)
        assertEquals(secondPlants, states[3].plants)
        assertEquals(2, states[3].plants?.size)
    }
}