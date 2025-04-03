package com.dahlaran.naturaquiz.presentation.viewmodel

/**
 * View events for the Quiz screen or Splash screen
 */
sealed class QuizViewEvent {
    data object OnArriveOnSplash : QuizViewEvent()
    data object OnArriveOnQuizScreen : QuizViewEvent()
    data object Refresh : QuizViewEvent()
    data class HandelAnswer(val isLeft: Boolean) : QuizViewEvent()
}