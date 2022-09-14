package com.desuzed.everyweather.domain.model.weather

import com.desuzed.everyweather.domain.model.ActionResult

data class ResultForecast(
    val weatherResponse: WeatherResponse?,
    val actionResult: ActionResult?
)