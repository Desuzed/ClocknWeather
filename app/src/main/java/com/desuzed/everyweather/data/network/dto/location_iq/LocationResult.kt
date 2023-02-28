package com.desuzed.everyweather.data.network.dto.location_iq

import com.google.gson.annotations.SerializedName

data class LocationResult(
    @SerializedName("lat")
    val lat: String,
    @SerializedName("lon")
    val lon: String,
    @SerializedName("display_name")
    val name: String,
    @SerializedName("importance")
    val importance: Float,
)
