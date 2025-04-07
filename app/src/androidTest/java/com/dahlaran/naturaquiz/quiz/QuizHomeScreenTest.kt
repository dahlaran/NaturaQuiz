package com.dahlaran.naturaquiz.presentation.home.quiz_screen

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.dahlaran.naturaquiz.core.data.AppError
import com.dahlaran.naturaquiz.domain.entities.Plant
import com.dahlaran.naturaquiz.domain.entities.Quiz
import com.dahlaran.naturaquiz.presentation.viewmodel.QuizState
import com.dahlaran.naturaquiz.presentation.viewmodel.QuizViewModel
import com.dahlaran.naturaquiz.ui.theme.NaturaQuizTheme
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class QuizHomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockViewModel: QuizViewModel
    private lateinit var stateFlow: MutableStateFlow<QuizState>

    @Before
    fun setup() {
        mockViewModel = mockk(relaxed = true)
        stateFlow = MutableStateFlow(QuizState())
        every { mockViewModel.state } returns stateFlow
    }

    @Test
    fun quizHomeScreenShowsLoadingState() {
        stateFlow.value = QuizState(isLoading = true)


        composeTestRule.setContent {
            NaturaQuizTheme {
                QuizHomeScreen(viewModel = mockViewModel)
            }
        }

        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }

    @Test
    fun quizHomeScreenShowsErrorState() {
        stateFlow.value = QuizState(error = AppError.NetworkError())

        composeTestRule.setContent {
            NaturaQuizTheme {
                QuizHomeScreen(viewModel = mockViewModel)
            }
        }

        composeTestRule.onNodeWithText("Network connection problem. Please check your connection.").assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").performClick()


        verify { mockViewModel.onEvent(com.dahlaran.naturaquiz.presentation.viewmodel.QuizViewEvent.Refresh) }
    }

    @Test
    fun quizHomeScreenShowsQuizContent() {
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

        val quiz = Quiz(goodPlant, wrongPlant, true)
        val nextQuiz = Quiz(wrongPlant, goodPlant, false)

        stateFlow.value = QuizState(
            isLoading = false,
            quiz = quiz,
            nextQuiz = nextQuiz,
            streak = 3
        )

        // Launch the UI
        composeTestRule.setContent {
            NaturaQuizTheme {
                QuizHomeScreen(viewModel = mockViewModel)
            }
        }

        composeTestRule.onNodeWithText("3").assertIsDisplayed()

        composeTestRule.onAllNodesWithTag("left_quiz_button").assertCountEquals(2)
        composeTestRule.onAllNodesWithTag("right_quiz_button").assertCountEquals(2)
    }
}