package com.desuzed.everyweather.data.network.dto.weatherApi

import com.google.gson.annotations.SerializedName

class CurrentDto {

    @SerializedName("temp_c")
    var tempC = 0f

    @SerializedName("temp_f")
    var tempF = 0f

    @SerializedName("condition")
    var conditionDto: ConditionDto? = null

    @SerializedName("wind_kph")
    var windSpeedKph = 0f

    @SerializedName("wind_mph")
    var windSpeedMph = 0f

    @SerializedName("pressure_mb")
    var pressureMb = 0f

    @SerializedName("pressure_in")
    var pressureInch = 0f

    @SerializedName("precip_mm")
    var precipMm = 0f

    @SerializedName("precip_in")
    var precipInch = 0f

    @SerializedName("humidity")
    var humidity: Int = 0

    @SerializedName("feelslike_c")
    var feelsLikeC = 0f

    @SerializedName("feelslike_f")
    var feelsLikeF = 0f


}