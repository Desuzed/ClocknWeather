package com.desuzed.everyweather.data.network.dto.weatherApi

import com.google.gson.annotations.SerializedName

class ForecastDto(
    @SerializedName("forecastday")
    val forecastday: ArrayList<ForecastDayDto>? = null,
)