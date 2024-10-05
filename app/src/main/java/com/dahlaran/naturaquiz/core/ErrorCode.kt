package com.dahlaran.naturaquiz.core

enum class ErrorCode(val value: Int) {
    CODE_NOT_DISPLAY(0),
    CODE_NETWORK_PROBLEM(10),
    CODE_HTTP_EXCEPTION(11),
    CODE_UNKNOWN_EXCEPTION(12),
    CODE_SUCCESS(200),
    CODE_NO_CONTENT(204),
    CODE_BAD_REQUEST(400),
    CODE_UNAUTHORIZED(401),
    CODE_PAYMENT_REQUIRED(402),
    CODE_FORBIDDEN(403),
    CODE_NOT_FOUND(404),
    CODE_TIMEOUT(408);

    companion object {
        fun fromInt(value: Int?): ErrorCode {
            if (value == null) {
                return CODE_UNKNOWN_EXCEPTION
            }
            for (errorCode in entries) {
                if (errorCode.value == value) {
                    return errorCode
                }
            }
            return CODE_UNKNOWN_EXCEPTION
        }
    }
}