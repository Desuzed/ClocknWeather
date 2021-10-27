package com.desuzed.clocknweather.network.dto

import com.google.gson.annotations.SerializedName

class Forecast {
    @SerializedName("forecastday")
    var forecastday: ArrayList<ForecastDay>? = null
}