package com.desuzed.everyweather.network.dto

import com.google.gson.annotations.SerializedName

class ForecastDto {
    @SerializedName("forecastday")
    var forecastday: ArrayList<ForecastDayDto>? = null
}