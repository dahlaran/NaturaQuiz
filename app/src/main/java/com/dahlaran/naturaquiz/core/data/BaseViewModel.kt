package com.dahlaran.naturaquiz.core.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber


abstract class BaseViewModel : ViewModel() {

    protected fun <T> launchUseCase(
        functionToLaunch: suspend () -> Flow<DataState<T>>,
        onSuccess: ((T) -> Unit)? = null,
        onError: ((AppError) -> Unit)? = null,
    ) {
        viewModelScope.launch {
            try {
                functionToLaunch().onEach { result ->
                    when (result) {
                        is DataState.Success -> {
                            onSuccess?.invoke(result.data)
                        }

                        is DataState.Error -> {
                            Timber.e("Error: ${result.error}")
                            onError?.invoke(result.error)
                        }
                    }
                }.launchIn(viewModelScope)
            } catch (e: Exception) {
                val error = ErrorMapper.mapThrowableToAppError(e)
                Timber.e("Unhandled exception: $e")
                onError?.invoke(error)
            }
        }
    }
}