package com.desuzed.everyweather.data.repository

import com.desuzed.everyweather.model.entity.WeatherResponse

class ResultForecast(
    val weatherResponse: WeatherResponse?,
    val message: String?
)