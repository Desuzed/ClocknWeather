package com.desuzed.everyweather.data.network.dto.weatherApi

import com.google.gson.annotations.SerializedName

data class WeatherResponseDto(
    @SerializedName("location")
    val locationDto: LocationDto? = null,
    @SerializedName("current")
    val currentDto: CurrentDto? = null,
    @SerializedName("forecast")
    val forecastDto: ForecastDto? = null,
)
