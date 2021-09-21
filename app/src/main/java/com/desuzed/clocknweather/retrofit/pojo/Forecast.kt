package com.desuzed.clocknweather.retrofit.pojo

import com.google.gson.annotations.SerializedName

class Forecast {
    @SerializedName("forecastday")
    var forecastday: ArrayList<ForecastDay>? = null
}