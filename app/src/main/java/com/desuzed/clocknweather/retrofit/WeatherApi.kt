package com.desuzed.clocknweather.retrofit

import com.desuzed.clocknweather.retrofit.pojo.Current
import com.desuzed.clocknweather.retrofit.pojo.Forecast
import com.desuzed.clocknweather.retrofit.pojo.Location
import com.google.gson.annotations.SerializedName

class WeatherApi {
    @SerializedName("location")
    var location: Location? = null

    @SerializedName("current")
    var current: Current? = null

    @SerializedName("forecast")
    var forecast: Forecast? = null

}