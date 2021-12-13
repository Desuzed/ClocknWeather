package com.desuzed.everyweather.data.network.dto.weatherApi

import com.desuzed.everyweather.data.network.dto.ApiTypeWeather
import com.google.gson.annotations.SerializedName

class WeatherResponseDto : ApiTypeWeather.WeatherApi()  {
    @SerializedName("location")
    var locationDto: LocationDto? = null

    @SerializedName("current")
    var currentDto: CurrentDto? = null

    @SerializedName("forecast")
    var forecastDto: ForecastDto? = null

}

