package com.dahlaran.naturaquiz.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dahlaran.naturaquiz.core.data.AppError
import com.dahlaran.naturaquiz.core.data.DataState
import com.dahlaran.naturaquiz.domain.entities.ListsHome
import com.dahlaran.naturaquiz.domain.entities.Plant
import com.dahlaran.naturaquiz.domain.entities.Specie
import com.dahlaran.naturaquiz.domain.usecases.GetListsHomeUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
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

        coEvery { getListsHomeUseCase.invoke() } returns DataState.Success(listsHome)

        assertEquals(false, viewModel.state.first().isLoading)
        assertNull(viewModel.state.first().plants)
        assertNull(viewModel.state.first().species)
        
        viewModel.onEvent(ListsViewEvent.OnArriveOnList)
        
        assertEquals(true, viewModel.state.first().isLoading)

        // Advance time
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(false, viewModel.state.first().isLoading)
        assertEquals(plants, viewModel.state.first().plants)
        assertEquals(species, viewModel.state.first().species)
        assertNull(viewModel.state.first().error)
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

        coEvery { getListsHomeUseCase.invoke() } returns DataState.Success(listsHome)
        
        viewModel.onEvent(ListsViewEvent.OnArriveOnSplash)

        // Advance time
        testDispatcher.scheduler.advanceUntilIdle()
        
        assertEquals(false, viewModel.state.first().isLoading)
        assertEquals(plants, viewModel.state.first().plants)
        assertEquals(species, viewModel.state.first().species)
        assertNull(viewModel.state.first().error)
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

        coEvery { getListsHomeUseCase.invoke() } returns DataState.Success(listsHome)

        viewModel.onEvent(ListsViewEvent.Refresh)
        
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(false, viewModel.state.first().isLoading)
        assertEquals(plants, viewModel.state.first().plants)
        assertEquals(species, viewModel.state.first().species)
        assertNull(viewModel.state.first().error)
    }

    @Test
    fun `fetchLists error updates state with error`() = runTest {
        val error = AppError.NetworkError()
        coEvery { getListsHomeUseCase.invoke() } returns DataState.Error(error)

        viewModel.onEvent(ListsViewEvent.Refresh)

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(false, viewModel.state.first().isLoading)
        assertEquals(error, viewModel.state.first().error)
        assertNull(viewModel.state.first().plants)
        assertNull(viewModel.state.first().species)
    }
}