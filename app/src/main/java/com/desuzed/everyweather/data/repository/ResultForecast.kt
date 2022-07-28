package com.desuzed.everyweather.data.repository

import com.desuzed.everyweather.domain.model.WeatherResponse

class ResultForecast(
    val weatherResponse: WeatherResponse?,
    val message: String?
)