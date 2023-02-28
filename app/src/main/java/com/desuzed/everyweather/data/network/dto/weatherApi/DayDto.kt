package com.desuzed.everyweather.data.network.dto.weatherApi

import com.google.gson.annotations.SerializedName

data class DayDto(
    @SerializedName("maxtemp_c")
    val maxTempC: Float = 0f,
    @SerializedName("maxtemp_f")
    val maxTempF: Float = 0f,
    @SerializedName("mintemp_c")
    val minTempC: Float = 0f,
    @SerializedName("mintemp_f")
    val minTempF: Float = 0f,
    @SerializedName("maxwind_kph")
    val maxWindKph: Float = 0f,
    @SerializedName("maxwind_mph")
    val maxWindMph: Float = 0f,
    @SerializedName("totalprecip_mm")
    val totalPrecipMm: Float = 0f,
    @SerializedName("totalprecip_in")
    val totalPrecipInch: Float = 0f,
    @SerializedName("avghumidity")
    val avgHumidity: Float = 0f,
    @SerializedName("daily_chance_of_rain")
    val popRain: Int = 0,
    @SerializedName("condition")
    val conditionDto: ConditionDto? = null,
)