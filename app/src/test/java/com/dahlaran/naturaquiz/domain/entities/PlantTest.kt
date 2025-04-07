package com.dahlaran.naturaquiz.domain.entities

import org.junit.Assert.*
import org.junit.Test

class PlantTest {

    @Test
    fun `isEligibleForQuiz returns true when plant has image and name`() {
        val plant = Plant(
            id = 1,
            name = "Test Plant",
            scientificName = "Testus plantus",
            imageUrl = "https://example.com/image.jpg",
            discoveredYear = 2020,
            family = "Test Family",
            genus = "Test"
        )

        assertTrue(plant.isEligibleForQuiz())
    }

    @Test
    fun `isEligibleForQuiz returns true when plant has image and scientificName but no name`() {
        val plant = Plant(
            id = 1,
            name = null,
            scientificName = "Testus plantus",
            imageUrl = "https://example.com/image.jpg",
            discoveredYear = 2020,
            family = "Test Family",
            genus = "Test"
        )

        assertTrue(plant.isEligibleForQuiz())
    }

    @Test
    fun `isEligibleForQuiz returns false when plant has no image`() {
        val plant = Plant(
            id = 1,
            name = "Test Plant",
            scientificName = "Testus plantus",
            imageUrl = null,
            discoveredYear = 2020,
            family = "Test Family",
            genus = "Test"
        )

        assertFalse(plant.isEligibleForQuiz())
    }

    @Test
    fun `isEligibleForQuiz returns false when plant has empty image`() {
        val plant = Plant(
            id = 1,
            name = "Test Plant",
            scientificName = "Testus plantus",
            imageUrl = "",
            discoveredYear = 2020,
            family = "Test Family",
            genus = "Test"
        )

        assertFalse(plant.isEligibleForQuiz())
    }

    @Test
    fun `isEligibleForQuiz returns false when plant has no name and no scientificName`() {
        val plant = Plant(
            id = 1,
            name = null,
            scientificName = null,
            imageUrl = "https://example.com/image.jpg",
            discoveredYear = 2020,
            family = "Test Family",
            genus = "Test"
        )

        assertFalse(plant.isEligibleForQuiz())
    }

    @Test
    fun `getNotNullName returns name when name is not null`() {
        val plant = Plant(
            id = 1,
            name = "Test Plant",
            scientificName = "Testus plantus",
            imageUrl = "https://example.com/image.jpg",
            discoveredYear = 2020,
            family = "Test Family",
            genus = "Test"
        )

        assertEquals("Test Plant", plant.getNotNullName())
    }

    @Test
    fun `getNotNullName returns scientificName when name is null`() {
        val plant = Plant(
            id = 1,
            name = null,
            scientificName = "Testus plantus",
            imageUrl = "https://example.com/image.jpg",
            discoveredYear = 2020,
            family = "Test Family",
            genus = "Test"
        )

        assertEquals("Testus plantus", plant.getNotNullName())
    }

    @Test
    fun `getNotNullName returns empty string when both name and scientificName are null`() {
        val plant = Plant(
            id = 1,
            name = null,
            scientificName = null,
            imageUrl = "https://example.com/image.jpg",
            discoveredYear = 2020,
            family = "Test Family",
            genus = "Test"
        )

        assertEquals("", plant.getNotNullName())
    }
}