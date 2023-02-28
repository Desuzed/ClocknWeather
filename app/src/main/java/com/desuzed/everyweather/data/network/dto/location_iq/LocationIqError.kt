package com.desuzed.everyweather.data.network.dto.location_iq

import com.google.gson.annotations.SerializedName

data class LocationIqError(
    @SerializedName("error")
    val error: String
)