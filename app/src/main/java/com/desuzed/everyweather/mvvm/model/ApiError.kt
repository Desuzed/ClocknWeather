package com.desuzed.everyweather.mvvm.model

class ApiError(val error: Error) {

    class Error(
        val code: Int,
        val message: String
    )
}