package com.dahlaran.naturaquiz.data

import com.dahlaran.naturaquiz.core.data.DataState
import com.dahlaran.naturaquiz.data.model.PlantsCountResponse
import com.dahlaran.naturaquiz.data.model.PlantsResponse
import com.dahlaran.naturaquiz.data.model.SpeciesResponse
import com.dahlaran.naturaquiz.domain.entities.Plant
import com.dahlaran.naturaquiz.domain.entities.Specie
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class PlantRepositoryTest {
    private lateinit var repository: PlantRepositoryImpl
    private lateinit var plantService: PlantService

    @Before
    fun setup() {
        plantService = mockk()
        repository = PlantRepositoryImpl(plantService)
    }

    @Test
    fun `getPlantsCount success should return count`() = runTest {
        // Setup
        val plantsCountResponse = PlantsCountResponse(100, 80)
        coEvery { plantService.getPlantsCount() } returns Response.success(plantsCountResponse)

        // When
        val result = repository.getPlantsCount()

        // Then
        assertTrue(result is DataState.Success)
        assertEquals(100, (result as DataState.Success).data)
    }

    @Test
    fun `getPlants success should return list of plants`() = runTest {
        // Setup
        val plants = listOf(
            Plant(1, "Plant1", "Scientific1", "url1", 2020, "Family1", "Genus1"),
            Plant(2, "Plant2", "Scientific2", "url2", 2021, "Family2", "Genus2")
        )
        val plantsResponse = PlantsResponse(plants)
        repository.plantNumber = 100 // Set plant number to avoid getPlantsCount call
        coEvery { plantService.getPlants(any()) } returns Response.success(plantsResponse)

        // When
        val result = repository.getPlants()

        // Then
        assertTrue(result is DataState.Success)
        assertEquals(plants, (result as DataState.Success).data)
    }

    @Test
    fun `getListsHome success should return combined lists`() = runTest {
        // Setup
        val plants = listOf(
            Plant(1, "Plant1", "Scientific1", "url1", 2020, "Family1", "Genus1")
        )
        val species = listOf(
            Specie(1, "Species1", "SciSpecies1", "url1", 2020, "Family1", "Genus1")
        )

        coEvery { plantService.getPlants(2) } returns Response.success(PlantsResponse(plants))
        coEvery { plantService.getSpecies() } returns Response.success(SpeciesResponse(species))

        // When
        val result = repository.getListsHome()

        // Then
        assertTrue(result is DataState.Success)
        assertEquals(plants, (result as DataState.Success).data.plants)
        assertEquals(species, result.data.species)
    }
}