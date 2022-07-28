package com.desuzed.everyweather.domain.model


class WeatherResponse(
    val location: Location,

    val current: Current,

    val forecastDay: List<ForecastDay>
)