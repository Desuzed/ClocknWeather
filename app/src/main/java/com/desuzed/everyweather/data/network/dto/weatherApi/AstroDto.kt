package com.desuzed.everyweather.data.network.dto.weatherApi


import com.google.gson.annotations.SerializedName

class AstroDto {
    @SerializedName("sunrise")
    var sunrise: String = ""

    @SerializedName("sunset")
    var sunset: String = ""

    @SerializedName("moonrise")
    var moonrise: String = ""

    @SerializedName("moonset")
    var moonset: String = ""

    @SerializedName("moon_phase")
    var moonPhase: String = ""

    @SerializedName("moon_illumination")
    var moonIllumination: String = ""

}