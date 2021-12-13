package com.desuzed.everyweather.model.model


class WeatherResponse(
    val location: Location,

    val current: Current,

    val forecastDay: List<ForecastDay>
)