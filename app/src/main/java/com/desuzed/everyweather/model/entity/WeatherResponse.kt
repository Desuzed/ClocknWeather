package com.desuzed.everyweather.model.entity


class WeatherResponse(
    val location: Location,

    val current: Current,

    val forecastDay: List<ForecastDay>
)