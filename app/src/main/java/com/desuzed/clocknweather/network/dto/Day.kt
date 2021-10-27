package com.desuzed.clocknweather.network.dto

import com.google.gson.annotations.SerializedName

class Day {

    @SerializedName("maxtemp_c")
    var maxTemp = 0f

    @SerializedName("mintemp_c")
    var minTemp = 0f

    @SerializedName("avgtemp_c")
    var avgTemp = 0f

    @SerializedName("maxwind_kph")
    var maxWind = 0f

    @SerializedName("totalprecip_mm")
    var totalPrecip = 0f

    @SerializedName("avgvis_km")
    var avgVis = 0f

    @SerializedName("avghumidity")
    var avgHumidity = 0f

    @SerializedName("daily_chance_of_rain")
    var popRain: Int = 0

    @SerializedName("daily_chance_of_snow")
    var popSnow: Int = 0

    @SerializedName("condition")
    var condition: Condition? = null

    @SerializedName("uv")
    var uv = 0f

    var pop : Int = (popRain + popSnow)/2
}