package com.desuzed.everyweather.mvvm.model


class WeatherResponse(
    val location: Location,

    val current: Current,

    val forecastDay: ArrayList<ForecastDay>
)