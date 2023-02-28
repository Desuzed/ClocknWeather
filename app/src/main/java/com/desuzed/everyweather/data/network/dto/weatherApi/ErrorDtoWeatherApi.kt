package com.desuzed.everyweather.data.network.dto.weatherApi

import com.google.gson.annotations.SerializedName


data class ErrorDtoWeatherApi(
    @SerializedName("error")
    val error: Error? = null,
)

data class Error(
    @SerializedName("code")
    val code: Int = 0,

    @SerializedName("message")
    val message: String = "",
)