package com.desuzed.everyweather.domain.model.weather

import kotlinx.serialization.Serializable

@Serializable
data class WeatherContent(
    val location: Location,
    val current: Current,
    val forecastDay: List<ForecastDay>,
)