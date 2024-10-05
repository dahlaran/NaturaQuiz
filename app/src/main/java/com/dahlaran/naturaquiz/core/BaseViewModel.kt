package com.dahlaran.naturaquiz.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


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
                    onError?.invoke(functionToLaunch.repoError)
                }
            }
        }
    }
}