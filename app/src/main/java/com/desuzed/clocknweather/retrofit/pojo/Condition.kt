package com.desuzed.clocknweather.retrofit.pojo

import com.desuzed.clocknweather.R
import com.google.gson.annotations.SerializedName

class Condition {
    @SerializedName("text")
    var text: String? = null
    @SerializedName("icon")
    var icon: String? = null
    @SerializedName("code")
    var code: Int = 0
    override fun toString(): String {
        return "test: $text)"
    }


}