package com.desuzed.everyweather.data.network.dto.weatherApi

import com.google.gson.annotations.SerializedName

class LocationDto {
    @SerializedName("name")
    var name: String = ""

    @SerializedName("region")
    var region: String= ""

    @SerializedName("country")
    var country: String= ""

    @SerializedName("lat")
    var lat = 0f

    @SerializedName("lon")
    var lon = 0f

    @SerializedName("tz_id")
    var timeZone: String = ""

    @SerializedName("localtime_epoch")
    var localtimeEpoch: Long = 0

    @SerializedName("localtime")
    var localtime: String = ""

}