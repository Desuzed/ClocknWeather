package com.desuzed.clocknweather.network.dto

import com.google.gson.annotations.SerializedName

class LocationDto {
    @SerializedName("name")
    var name: String? = null

    @SerializedName("region")
    var region: String? = null

    @SerializedName("country")
    var country: String? = null

    @SerializedName("lat")
    var lat = 0f

    @SerializedName("lon")
    var lon = 0f

    @SerializedName("tz_id")
    var tzId: String? = null

    @SerializedName("localtime_epoch")
    var localtime_epoch: Long = 0

    @SerializedName("localtime")
    var localtime: String? = null
    override fun toString(): String {
        return "Location(name=$name, \nregion=$region, \ncountry=$country, \nlat=$lat, \nlon=$lon, \ntz_id=$tzId, \nlocaltime_epoch=$localtime_epoch, \nlocaltime=$localtime)"
    }

    fun toStringLatLon (): String{
        return "$lat,$lon"
    }

}