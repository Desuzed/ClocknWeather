package com.desuzed.everyweather.domain.model.weather

import com.desuzed.everyweather.data.repository.providers.action_result.QueryResult

data class ResultForecast(
    val weatherResponse: WeatherResponse?,
    val queryResult: QueryResult?
)