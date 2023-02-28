package com.desuzed.everyweather.domain.model.weather

data class ApiError(val error: Error)
data class Error(val code: Int, val message: String)