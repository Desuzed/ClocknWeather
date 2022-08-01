package com.desuzed.everyweather.data.network.dto.weatherApi

import com.google.gson.annotations.SerializedName


class ErrorDtoWeatherApi {
    @SerializedName("error")
    var error: Error? = null

    class Error {
        @SerializedName("code")
        var code = 0

        @SerializedName("message")
        var message: String = ""
    }
}