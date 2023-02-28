package com.desuzed.everyweather.data.network.dto.weatherApi

import com.google.gson.annotations.SerializedName

data class HourDto(
    @SerializedName("time_epoch")
    val timeEpoch: Long = 0,
    @SerializedName("temp_c")
    val tempC: Float = 0f,
    @SerializedName("temp_f")
    val tempF: Float = 0f,
    @SerializedName("condition")
    val conditionDto: ConditionDto? = null,
    @SerializedName("wind_kph")
    val windSpeedKph: Float = 0f,
    @SerializedName("wind_mph")
    val windSpeedMph: Float = 0f,
    @SerializedName("wind_degree")
    val windDegree: Int = 0,
    @SerializedName("pressure_mb")
    val pressureMb: Float = 0f,
    @SerializedName("pressure_in")
    val pressureInch: Float = 0f,
    @SerializedName("humidity")
    val humidity: Int = 0,
)