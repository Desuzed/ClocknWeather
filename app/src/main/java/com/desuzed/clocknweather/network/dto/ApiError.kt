package com.desuzed.clocknweather.network.dto

import com.google.gson.annotations.SerializedName


class ApiError {
    @SerializedName("error")
    var error : Error? = null
    class Error {
        @SerializedName("code")
        var code = 0
        @SerializedName("message")
        var message: String = ""
    }
}