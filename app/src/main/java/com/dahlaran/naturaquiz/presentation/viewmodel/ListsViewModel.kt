package com.dahlaran.naturaquiz.presentation.viewmodel

import com.dahlaran.naturaquiz.core.data.BaseViewModel
import com.dahlaran.naturaquiz.domain.usecases.GetListsHomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * ViewModel for the lists screen (Home screen)
 */
@HiltViewModel
class ListsViewModel @Inject constructor(
    private val getListsHomeUseCase: GetListsHomeUseCase
) : BaseViewModel() {

    private val _state = MutableStateFlow(ListsHomeState())
    val state = _state.asStateFlow()

    fun onEvent(event: ListsViewEvent) {
        when (event) {
            is ListsViewEvent.OnArriveOnList,
            is ListsViewEvent.OnArriveOnSplash,
            is ListsViewEvent.Refresh -> {
                fetchLists()
            }
        }
    }

    private fun fetchLists() {
        _state.update { it.copy(isLoading = true) }
        launchUseCase({ getListsHomeUseCase.invoke() }, onSuccess = { lists ->
            _state.update {
                it.copy(
                    isLoading = false,
                    plants = lists.plants,
                    species = lists.species,
                    error = null
                )
            }
        }, onError = { error ->
            _state.update { it.copy(isLoading = false, error = error) }
        })
    }
}