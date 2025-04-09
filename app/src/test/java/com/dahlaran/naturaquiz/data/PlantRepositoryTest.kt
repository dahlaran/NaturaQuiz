package com.dahlaran.naturaquiz.data

import app.cash.turbine.test
import com.dahlaran.naturaquiz.core.data.AppError
import com.dahlaran.naturaquiz.core.data.DataState
import com.dahlaran.naturaquiz.core.network.NetworkChecker
import com.dahlaran.naturaquiz.data.model.PlantsResponse
import com.dahlaran.naturaquiz.data.model.SpeciesResponse
import com.dahlaran.naturaquiz.domain.entities.Plant
import com.dahlaran.naturaquiz.domain.entities.Specie
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class PlantRepositoryTest {
    private lateinit var repository: PlantRepositoryImpl
    private lateinit var plantService: PlantService
    private lateinit var networkChecker: NetworkChecker
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        plantService = mockk()
        networkChecker = mockk()
        repository = PlantRepositoryImpl(
            plantService = plantService,
            networkChecker = networkChecker
        )
        every { networkChecker.isConnected() } returns true
    }

    @Test
    fun `getPlants success should emit loading and then plant list`() = runTest(testDispatcher) {
        val plants = listOf(
            Plant(1, "Plant1", "Scientific1", "url1", 2020, "Family1", "Genus1"),
            Plant(2, "Plant2", "Scientific2", "url2", 2021, "Family2", "Genus2")
        )
        val plantsResponse = PlantsResponse(plants)

        repository.plantNumber = 100
        coEvery { plantService.getPlants(any()) } returns Response.success(plantsResponse)

        repository.getPlants().test {
            val successState = awaitItem()
            assertTrue(successState is DataState.Success)
            assertEquals(plants, (successState as DataState.Success).data)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getPlants with network error should emit error state`() = runTest(testDispatcher) {
        repository.plantNumber = 100
        every { networkChecker.isConnected() } returns true
        coEvery { plantService.getPlants(any()) } throws java.io.IOException("Network error")

        repository.getPlants().test {

            val errorState = awaitItem()
            assertTrue(errorState is DataState.Error)
            assertTrue((errorState as DataState.Error).error is AppError.NetworkError)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getPlants with no connection should emit no connection error`() = runTest(testDispatcher) {
        repository.plantNumber = 100
        every { networkChecker.isConnected() } returns false

        repository.getPlants().test {

            val errorState = awaitItem()
            assertTrue(errorState is DataState.Error)
            assertTrue((errorState as DataState.Error).error is AppError.NoInternetConnection)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getListsHome success should emit loading and then combined lists`() =
        runTest(testDispatcher) {
            val plants = listOf(
                Plant(1, "Plant1", "Scientific1", "url1", 2020, "Family1", "Genus1")
            )
            val species = listOf(
                Specie(1, "Species1", "SciSpecies1", "url1", 2020, "Family1", "Genus1")
            )

            coEvery { plantService.getPlants(2) } returns Response.success(PlantsResponse(plants))
            coEvery { plantService.getSpecies() } returns Response.success(SpeciesResponse(species))

            repository.getListsHome().test {
                val successState = awaitItem()
                assertTrue(successState is DataState.Success)
                assertEquals(plants, (successState as DataState.Success).data.plants)
                assertEquals(species, successState.data.species)

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `getListsHome with network error should emit network error`() = runTest(testDispatcher) {
        coEvery { plantService.getPlants(any()) } throws java.io.IOException("Network error")

        repository.getListsHome().test {
            val errorState = awaitItem()
            assertTrue(errorState is DataState.Error)
            assertTrue((errorState as DataState.Error).error is AppError.NetworkError)

            cancelAndIgnoreRemainingEvents()
        }
    }
}