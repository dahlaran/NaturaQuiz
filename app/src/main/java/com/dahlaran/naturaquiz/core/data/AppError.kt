package com.dahlaran.naturaquiz.core.data

sealed class AppError {
    data class NetworkError(val exception: Exception? = null) : AppError()
    data class ApiError(val code: Int, val message: String) : AppError()
    data class ResourceNotFoundError(val resourceType: String) : AppError()
    data class ValidationError(val field: String, val message: String) : AppError()
    data object DatabaseError : AppError()
    data class UnexpectedError(val exception: Exception? = null) : AppError()
    data object NoInternetConnection : AppError()
    data object TimeoutError : AppError()
    data object EmptyResultError : AppError()
}