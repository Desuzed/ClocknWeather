package com.desuzed.everyweather.data.repository

import com.desuzed.everyweather.model.entity.WeatherResponse

class ResultForecast(
    private val weatherResponse: WeatherResponse?,
    private val message: String?
) {
    fun getMessage(): String? = message
    fun getWeatherResponse(): WeatherResponse? = weatherResponse
}