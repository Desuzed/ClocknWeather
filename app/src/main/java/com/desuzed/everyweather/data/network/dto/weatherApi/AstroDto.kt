package com.desuzed.everyweather.data.network.dto.weatherApi


import android.annotation.SuppressLint
import com.desuzed.everyweather.domain.model.Astro
import com.desuzed.everyweather.util.EntityMapper
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat

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


class AstroMapper : EntityMapper<AstroDto, Astro> {
    //todo вынести в SimpleDateUtil Formatter
    @SuppressLint("SimpleDateFormat")
    private val apiFormat = SimpleDateFormat("hh:mm a")
    @SuppressLint("SimpleDateFormat")
    private val appFormat = SimpleDateFormat("HH:mm")

    override fun mapFromEntity(entity: AstroDto): Astro {
        return try {
            val apiSunrise = apiFormat.parse(entity.sunrise)
            val apiSunset = apiFormat.parse(entity.sunset)
            val apiMoonrise = apiFormat.parse(entity.moonrise)
            val apiMoonset = apiFormat.parse(entity.moonset)
            val sunrise = appFormat.format(apiSunrise)
            val sunset = appFormat.format(apiSunset)
            val moonrise = appFormat.format(apiMoonrise)
            val moonset = appFormat.format(apiMoonset)
            Astro(sunrise, sunset, moonrise, moonset)
        }catch (e: Exception){
            Astro(entity.sunrise, entity.sunset, entity.moonrise, entity.moonset)
        }

    }
}