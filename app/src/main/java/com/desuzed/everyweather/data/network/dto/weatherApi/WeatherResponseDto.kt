package com.desuzed.everyweather.data.network.dto.weatherApi

import com.google.gson.annotations.SerializedName

class WeatherResponseDto {
    @SerializedName("location")
    var locationDto: LocationDto? = null

    @SerializedName("current")
    var currentDto: CurrentDto? = null

    @SerializedName("forecast")
    var forecastDto: ForecastDto? = null

}
