package com.dahlaran.naturaquiz.core

import android.accounts.NetworkErrorException
import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import com.dahlaran.naturaquiz.R
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import kotlin.reflect.KFunction

data class RepoError(val status: ErrorCode, val function: KFunction<*>? = null) {

    companion object {

        /**
         * Create an Error from a ResponseBody
         *
         * @param responseBody ResponseBody to convert
         */
        fun fromResponseBody(responseBody: ResponseBody?): RepoError {
            return try {
                val errorString = responseBody?.toString()?.toInt()
                val repoError = RepoError(ErrorCode.fromInt(errorString))
                repoError
            } catch (e: Exception) {
                RepoError(ErrorCode.CODE_UNKNOWN_EXCEPTION)
            }
        }

        /**
         * Create an Error from an Exception
         *
         * @param exception Exception to convert
         */
        fun fromException(exception: Exception): RepoError {
            return when (exception) {
                is NetworkErrorException, is IOException -> RepoError(ErrorCode.CODE_NETWORK_PROBLEM)
                is HttpException -> RepoError(ErrorCode.CODE_HTTP_EXCEPTION)
                else -> RepoError(ErrorCode.CODE_UNKNOWN_EXCEPTION)
            }
        }
    }

    /**
     * Show the error in a Toast
     *
     * @param context Context to show the Toast
     */
    fun showUsingCodeOnly(context: Context) {
        @StringRes val resId = when (status) {
            ErrorCode.CODE_NETWORK_PROBLEM -> R.string.error_no_network
            ErrorCode.CODE_HTTP_EXCEPTION -> R.string.error_http_exception
            ErrorCode.CODE_SUCCESS -> R.string.error_success
            ErrorCode.CODE_NO_CONTENT -> R.string.error_no_content
            ErrorCode.CODE_BAD_REQUEST -> R.string.error_bad_request
            ErrorCode.CODE_UNAUTHORIZED -> R.string.error_unauthorized
            ErrorCode.CODE_PAYMENT_REQUIRED -> R.string.error_payment_required
            ErrorCode.CODE_FORBIDDEN -> R.string.error_forbidden
            ErrorCode.CODE_NOT_FOUND -> R.string.error_no_found
            ErrorCode.CODE_TIMEOUT -> R.string.error_time_out
            ErrorCode.CODE_UNKNOWN_EXCEPTION -> R.string.error_default
            ErrorCode.CODE_NOT_DISPLAY -> return  // Do nothing
        }

        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show()
    }
}