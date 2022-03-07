package com.desuzed.everyweather.data.network.dto.weatherApi

import com.google.gson.annotations.SerializedName

class ConditionDto {
    @SerializedName("text")
    var text: String = ""

    @SerializedName("icon")
    var icon: String = ""

    @SerializedName("code")
    var code: Int = 0
    override fun toString(): String {
        return "test: $text)"
    }


}