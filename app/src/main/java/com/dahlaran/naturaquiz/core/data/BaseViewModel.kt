package com.dahlaran.naturaquiz.core.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dahlaran.naturaquiz.core.bus.Event
import com.dahlaran.naturaquiz.core.bus.EventBus
import kotlinx.coroutines.launch
import timber.log.Timber


abstract class BaseViewModel : ViewModel() {

    protected fun <T> launchUsesCase(
        functionToLaunch: DataState<T>,
        onSuccess: ((T) -> Unit)? = null,
        onError: ((RepoError) -> Unit)? = null,
    ) {
        viewModelScope.launch {
            when (functionToLaunch) {
                is DataState.Success -> {
                    onSuccess?.invoke(functionToLaunch.data)
                }

                is DataState.Error -> {
                    Timber.e(functionToLaunch.repoError.status.toString())
                    EventBus.sendEvent(Event.ToastError(functionToLaunch.repoError))
                    onError?.invoke(functionToLaunch.repoError)
                }
            }
        }
    }

    protected fun sendEvent(event: Event) {
        viewModelScope.launch {
            EventBus.sendEvent(event)
        }
    }
}