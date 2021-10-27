package com.desuzed.clocknweather.network.dto

import com.google.gson.annotations.SerializedName

class WeatherApi {
    @SerializedName("location")
    var locationDto: LocationDto? = null

    @SerializedName("current")
    var current: Current? = null

    @SerializedName("forecast")
    var forecast: Forecast? = null

}