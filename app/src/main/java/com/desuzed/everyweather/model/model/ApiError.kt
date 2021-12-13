package com.desuzed.everyweather.model.model

class ApiError(val error: Error) {

    class Error(
        val code: Int,
        val message: String
    )
}