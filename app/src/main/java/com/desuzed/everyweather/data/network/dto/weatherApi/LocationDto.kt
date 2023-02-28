package com.desuzed.everyweather.data.network.dto.weatherApi

import com.google.gson.annotations.SerializedName

data class LocationDto(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("region")
    val region: String = "",
    @SerializedName("country")
    val country: String = "",
    @SerializedName("lat")
    val lat: Float = 0f,
    @SerializedName("lon")
    val lon: Float = 0f,
    @SerializedName("tz_id")
    val timeZone: String = "",
    @SerializedName("localtime_epoch")
    val localtimeEpoch: Long = 0,
    @SerializedName("localtime")
    val localtime: String = ""
)