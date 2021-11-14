package com.desuzed.everyweather.network.dto

import com.google.gson.annotations.SerializedName

class ForecastDayDto {
    @SerializedName("date")
    var date: String = ""

    @SerializedName("date_epoch")
    var dateEpoch: Long = 0

    @SerializedName("day")
    var day: DayDto? = null

    @SerializedName("astro")
    var astroDto: AstroDto? = null

    @SerializedName("hour")
    var hourDto: ArrayList<HourDto>? = null
}