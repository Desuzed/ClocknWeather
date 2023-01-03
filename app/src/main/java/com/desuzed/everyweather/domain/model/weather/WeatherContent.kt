package com.desuzed.everyweather.domain.model.weather


data class WeatherContent(
    val location: Location,

    val current: Current,

    val forecastDay: List<ForecastDay>
)