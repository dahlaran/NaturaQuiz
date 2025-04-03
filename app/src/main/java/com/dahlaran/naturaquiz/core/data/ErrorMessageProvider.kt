package com.dahlaran.naturaquiz.core.data

import android.content.Context
import com.dahlaran.naturaquiz.R

class ErrorMessageProvider(private val context: Context) {
    fun getErrorMessage(error: AppError): String {
        return when (error) {
            is AppError.NetworkError -> context.getString(R.string.error_network)
            is AppError.NoInternetConnection -> context.getString(R.string.error_no_internet)
            is AppError.TimeoutError -> context.getString(R.string.error_timeout)
            is AppError.ApiError -> {
                when (error.code) {
                    404 -> context.getString(R.string.error_not_found)
                    401, 403 -> context.getString(R.string.error_unauthorized)
                    in 500..599 -> context.getString(R.string.error_server)
                    else -> context.getString(R.string.error_api, error.message)
                }
            }
            is AppError.ResourceNotFoundError ->
                context.getString(R.string.error_resource_not_found, error.resourceType)
            is AppError.ValidationError ->
                context.getString(R.string.error_validation, error.field, error.message)
            is AppError.DatabaseError -> context.getString(R.string.error_database)
            is AppError.UnexpectedError -> context.getString(R.string.error_unexpected)
            is AppError.EmptyResultError -> context.getString(R.string.error_empty_result)
        }
    }
}