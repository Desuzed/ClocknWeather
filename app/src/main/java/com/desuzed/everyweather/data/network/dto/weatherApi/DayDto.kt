package com.desuzed.everyweather.data.network.dto.weatherApi

import com.google.gson.annotations.SerializedName

class DayDto {

    @SerializedName("maxtemp_c")
    var maxTempC = 0f

    @SerializedName("maxtemp_f")
    var maxTempF = 0f

    @SerializedName("mintemp_c")
    var minTempC = 0f

    @SerializedName("mintemp_f")
    var minTempF = 0f

    @SerializedName("maxwind_kph")
    var maxWindKph = 0f

    @SerializedName("maxwind_mph")
    var maxWindMph = 0f

    @SerializedName("totalprecip_mm")
    var totalPrecipMm = 0f

    @SerializedName("totalprecip_in")
    var totalPrecipInch = 0f

    @SerializedName("avghumidity")
    var avgHumidity = 0f

    @SerializedName("daily_chance_of_rain")
    var popRain: Int = 0

    @SerializedName("condition")
    var conditionDto: ConditionDto? = null

}