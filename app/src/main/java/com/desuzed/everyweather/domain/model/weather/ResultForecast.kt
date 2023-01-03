package com.desuzed.everyweather.domain.model.weather

import com.desuzed.everyweather.domain.model.result.QueryResult

data class ResultForecast(
    val weatherContent: WeatherContent?,
    val queryResult: QueryResult?
)