package com.desuzed.everyweather.domain.model.weather

class ApiError(val error: Error) {

    class Error(
        val code: Int,
        val message: String
    )
}