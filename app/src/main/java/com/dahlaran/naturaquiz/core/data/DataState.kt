package com.dahlaran.naturaquiz.core.data

/**
 * DataState is a sealed class that can be used to handle the state of a data
 * It can be used to handle the state of a data in a ViewModel
 */
sealed class DataState<out T> {
    /**
     * Success state
     *
     * @param data : The data that was loaded
     */
    data class Success<out T>(val data: T) : DataState<T>()

    /**
     * Error state
     *
     * @param error : The error that occurred
     * @param message : The error message
     */
    data class Error(val error: AppError, val message: String? = null) : DataState<Nothing>()
}