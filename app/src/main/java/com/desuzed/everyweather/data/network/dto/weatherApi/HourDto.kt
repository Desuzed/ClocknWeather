package com.desuzed.everyweather.data.network.dto.weatherApi

import com.google.gson.annotations.SerializedName

class HourDto {

    @SerializedName("time_epoch")
    var timeEpoch: Long = 0

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

    @SerializedName("wind_degree")
    var windDegree: Int = 0

    @SerializedName("pressure_mb")
    var pressureMb = 0f

    @SerializedName("pressure_in")
    var pressureInch = 0f

    @SerializedName("humidity")
    var humidity: Int = 0

}