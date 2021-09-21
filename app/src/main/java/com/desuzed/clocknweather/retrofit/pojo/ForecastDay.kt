package com.desuzed.clocknweather.retrofit.pojo

import com.google.gson.annotations.SerializedName

class ForecastDay {
    @SerializedName("date")
    var date: String? = null

    @SerializedName("date_epoch")
    var dateEpoch: Long = 0

    @SerializedName("day")
    var day: Day? = null

    @SerializedName("astro")
    var astro: Astro? = null

    @SerializedName("hour")
    var hour: ArrayList<Hour>? = null
}