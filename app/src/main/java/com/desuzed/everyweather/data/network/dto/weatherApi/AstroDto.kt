package com.desuzed.everyweather.data.network.dto.weatherApi

import com.desuzed.everyweather.domain.model.Astro
import com.desuzed.everyweather.util.DateFormatter
import com.desuzed.everyweather.util.EntityMapper
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

class AstroMapper : EntityMapper<AstroDto, Astro> {

    override fun mapFromEntity(entity: AstroDto): Astro {
        return try {
            val apiSunrise = DateFormatter.parse(DateFormatter.apiTimePattern, entity.sunrise)
            val apiSunset = DateFormatter.parse(DateFormatter.apiTimePattern, entity.sunset)
            val apiMoonrise = DateFormatter.parse(DateFormatter.apiTimePattern, entity.moonrise)
            val apiMoonset = DateFormatter.parse(DateFormatter.apiTimePattern, entity.moonset)

            val sunrise = DateFormatter.format(DateFormatter.timePattern, apiSunrise)
            val sunset = DateFormatter.format(DateFormatter.timePattern, apiSunset)
            val moonrise = DateFormatter.format(DateFormatter.timePattern, apiMoonrise)
            val moonset = DateFormatter.format(DateFormatter.timePattern, apiMoonset)

            Astro(sunrise, sunset, moonrise, moonset)
        } catch (e: Exception) {
            Astro(entity.sunrise, entity.sunset, entity.moonrise, entity.moonset)
        }

    }
}