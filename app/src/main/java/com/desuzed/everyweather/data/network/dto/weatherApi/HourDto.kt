package com.desuzed.everyweather.data.network.dto.weatherApi

import com.google.gson.annotations.SerializedName

class HourDto {

    @SerializedName("time_epoch")
    var timeEpoch: Long = 0

    @SerializedName("time")
    var time: String = ""

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

    @SerializedName("wind_dir")
    var windDir: String = ""

    //todo handle settings
    @SerializedName("pressure_mb")
    var pressureMb = 0f

    @SerializedName("precip_mm")
    var precipMm = 0f

    @SerializedName("humidity")
    var humidity: Int = 0

    @SerializedName("cloud")
    var cloud: Int = 0

    @SerializedName("feelslike_c")
    var feelsLike = 0f

    @SerializedName("chance_of_rain")
    var chanceOfRain: Int = 0

    @SerializedName("chance_of_snow")
    var chanceOfSnow: Int = 0

    @SerializedName("vis_km")
    var vis = 0f

    @SerializedName("uv")
    var uv = 0f

    @SerializedName("gust_kph")
    var gust = 0f
}