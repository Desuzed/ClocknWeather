package com.desuzed.everyweather.data.network.dto.weatherApi

import com.google.gson.annotations.SerializedName

data class ForecastDayDto(
    @SerializedName("date")
    val date: String = "",
    @SerializedName("date_epoch")
    val dateEpoch: Long = 0,
    @SerializedName("day")
    val day: DayDto? = null,
    @SerializedName("astro")
    val astroDto: AstroDto? = null,
    @SerializedName("hour")
    val hourDto: ArrayList<HourDto>? = null,
)