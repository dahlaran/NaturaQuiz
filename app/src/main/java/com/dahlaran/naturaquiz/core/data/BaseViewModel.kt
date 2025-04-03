package com.dahlaran.naturaquiz.core.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber


abstract class BaseViewModel : ViewModel() {

    protected fun <T> launchUseCase(
        functionToLaunch: suspend () -> DataState<T>,
        onSuccess: ((T) -> Unit)? = null,
        onError: ((AppError) -> Unit)? = null,
    ) {
        viewModelScope.launch {
            try {
                when (val result = functionToLaunch()) {
                    is DataState.Success -> {
                        onSuccess?.invoke(result.data)
                    }
                    is DataState.Error -> {
                        Timber.e("Error: ${result.error}")
                        onError?.invoke(result.error)
                    }
                }
            } catch (e: Exception) {
                val error = ErrorMapper.mapThrowableToAppError(e)
                Timber.e("Unhandled exception: $e")
                onError?.invoke(error)
            }
        }
    }
}