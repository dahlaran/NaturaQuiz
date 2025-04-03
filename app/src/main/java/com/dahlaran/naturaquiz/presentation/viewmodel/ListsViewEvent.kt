package com.dahlaran.naturaquiz.presentation.viewmodel

/**
 * View events for the Lists screen or Splash screen.
 */
sealed class ListsViewEvent {
    data object OnArriveOnSplash : ListsViewEvent()
    data object OnArriveOnList : ListsViewEvent()
    data object Refresh : ListsViewEvent()
}