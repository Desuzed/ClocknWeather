package com.desuzed.clocknweather.retrofit.pojo

import com.desuzed.clocknweather.retrofit.WeatherApi
import com.google.gson.annotations.SerializedName

class Current {
    @SerializedName("last_updated_epoch")
    var lastUpdatedEpoch: Long = 0

    @SerializedName("last_updated")
    var lastUpdated: String? = null

    @SerializedName("temp_c")
    var temp = 0f

    @SerializedName("condition")
    var condition: Condition? = null

    @SerializedName("wind_kph")
    var windSpeed = 0f

    @SerializedName("wind_degree")
    var windDegree: Int = 0

    @SerializedName("wind_dir")
    var windDir: String? = null

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

    @SerializedName("vis_km")
    var vis = 0f

    @SerializedName("uv")
    var uv = 0f

    @SerializedName("gust_kph")
    var gust = 0f

}