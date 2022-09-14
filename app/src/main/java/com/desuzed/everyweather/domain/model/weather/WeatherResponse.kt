package com.desuzed.everyweather.domain.model.weather


class WeatherResponse(
    val location: Location,

    val current: Current,

    val forecastDay: List<ForecastDay>
)