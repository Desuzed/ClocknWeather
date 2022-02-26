package com.desuzed.everyweather.data.repository

import com.desuzed.everyweather.model.model.WeatherResponse

class ResultForecast(
    private val weatherResponse: WeatherResponse?,
    private val errorMessage: String?
) {
    fun getErrorMessage(): String? = errorMessage
    fun getWeatherResponse(): WeatherResponse? = weatherResponse
}