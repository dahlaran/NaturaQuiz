package com.dahlaran.naturaquiz.core.data

import android.accounts.NetworkErrorException
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

object ErrorMapper {
    /**
     * Maps a [Throwable] to an [AppError].
     *
     * This function handles different types of throwable and maps them to specific [AppError] types.
     *
     * @param throwable : The throwable to map.
     * @return An [AppError] representing the error.
     */
    fun mapThrowableToAppError(throwable: Throwable): AppError {
        return when (throwable) {
            is IOException, is NetworkErrorException -> {
                if (throwable.message?.contains("timeout") == true) {
                    AppError.TimeoutError
                } else {
                    AppError.NetworkError(throwable as? Exception)
                }
            }
            is HttpException -> {
                when (throwable.code()) {
                    404 -> AppError.ResourceNotFoundError("Resource")
                    401, 403 -> AppError.ApiError(
                        throwable.code(),
                        "Authentication error"
                    )
                    500, 501, 502, 503 -> AppError.ApiError(
                        throwable.code(),
                        "Server error"
                    )
                    else -> AppError.ApiError(
                        throwable.code(),
                        throwable.message() ?: "Unknown API error"
                    )
                }
            }
            else -> AppError.UnexpectedError(throwable as? Exception)
        }
    }

    /**
     * Maps a [Response] to an [AppError].
     *
     * This function handles different types of response errors and maps them to specific [AppError] types.
     *
     * @param response : The response to map.
     * @return An [AppError] representing the error.
     */
    fun mapResponseToAppError(response: Response<*>): AppError {
        return when (response.code()) {
            404 -> AppError.ResourceNotFoundError("Resource")
            401 -> AppError.ApiError(401, "Unauthorized access")
            403 -> AppError.ApiError(403, "Forbidden access")
            in 500..599 -> AppError.ApiError(
                response.code(),
                "Server error (${response.code()})"
            )
            else -> {
                val errorBody = response.errorBody()?.string()
                AppError.ApiError(
                    response.code(),
                    errorBody ?: "Unknown API error with code: ${response.code()}"
                )
            }
        }
    }
}