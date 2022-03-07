package com.desuzed.everyweather.model.entity

class ApiError(val error: Error) {

    class Error(
        val code: Int,
        val message: String
    )
}