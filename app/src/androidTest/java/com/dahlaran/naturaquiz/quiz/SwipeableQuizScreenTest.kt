package com.dahlaran.naturaquiz.quiz

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.dahlaran.naturaquiz.domain.entities.Plant
import com.dahlaran.naturaquiz.domain.entities.Quiz
import com.dahlaran.naturaquiz.presentation.home.quiz_screen.SwipeableQuizScreen
import com.dahlaran.naturaquiz.ui.theme.NaturaQuizTheme
import org.junit.Rule
import org.junit.Test

class SwipeableQuizScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun swipeableQuizScreenDisplaysQuizContent() {
        val goodPlant = Plant(
            id = 1,
            name = "Good Plant",
            scientificName = "Scientific Name 1",
            imageUrl = "https://example.com/image1.jpg",
            discoveredYear = 2020,
            family = "Family 1",
            genus = "Genus 1"
        )

        val wrongPlant = Plant(
            id = 2,
            name = "Wrong Plant",
            scientificName = "Scientific Name 2",
            imageUrl = "https://example.com/image2.jpg",
            discoveredYear = 2021,
            family = "Family 2",
            genus = "Genus 2"
        )

        val currentQuiz = Quiz(goodPlant, wrongPlant, true)
        val nextQuiz = Quiz(wrongPlant, goodPlant, false)

        var leftSwipeCount = 0
        var rightSwipeCount = 0

        composeTestRule.setContent {
            NaturaQuizTheme {
                SwipeableQuizScreen(
                    currentQuiz = currentQuiz,
                    nextQuiz = nextQuiz,
                    streak = 5,
                    onSwipeLeft = { leftSwipeCount++ },
                    onSwipeRight = { rightSwipeCount++ }
                )
            }
        }

        composeTestRule.onNodeWithText("5").assertIsDisplayed()

        composeTestRule.onAllNodesWithTag("left_quiz_button").assertCountEquals(2)
        composeTestRule.onAllNodesWithTag("right_quiz_button").assertCountEquals(2)

        assert(leftSwipeCount == 0)
        assert(rightSwipeCount == 0)

        composeTestRule.onAllNodesWithTag("left_quiz_button").onFirst().performClick()
        assert(leftSwipeCount == 0)
        assert(rightSwipeCount == 0)

        composeTestRule.onAllNodesWithTag("right_quiz_button").onFirst().performClick()
        assert(leftSwipeCount == 0)
        assert(rightSwipeCount == 0)
    }

    @Test
    fun swipeableQuizScreenDisplaysStreak() {
        val goodPlant = Plant(
            id = 1,
            name = "Good Plant",
            scientificName = "Scientific Name 1",
            imageUrl = "https://example.com/image1.jpg",
            discoveredYear = 2020,
            family = "Family 1",
            genus = "Genus 1"
        )

        val wrongPlant = Plant(
            id = 2,
            name = "Wrong Plant",
            scientificName = "Scientific Name 2",
            imageUrl = "https://example.com/image2.jpg",
            discoveredYear = 2021,
            family = "Family 2",
            genus = "Genus 2"
        )

        val currentQuiz = Quiz(goodPlant, wrongPlant, true)

        composeTestRule.setContent {
            NaturaQuizTheme {
                SwipeableQuizScreen(
                    currentQuiz = currentQuiz,
                    nextQuiz = currentQuiz,
                    streak = 10,
                    onSwipeLeft = { },
                    onSwipeRight = { }
                )
            }
        }

        composeTestRule.onNodeWithText("10").assertIsDisplayed()
    }
}