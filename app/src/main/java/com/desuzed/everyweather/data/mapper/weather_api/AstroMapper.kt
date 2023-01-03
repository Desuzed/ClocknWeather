package com.desuzed.everyweather.data.mapper.weather_api

import com.desuzed.everyweather.data.mapper.EntityMapper
import com.desuzed.everyweather.data.network.dto.weatherApi.AstroDto
import com.desuzed.everyweather.domain.model.weather.Astro
import com.desuzed.everyweather.util.DateFormatter

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

            Astro(sunrise = sunrise, sunset = sunset, moonrise = moonrise, moonset = moonset)
        } catch (e: Exception) {
            Astro(
                sunrise = entity.sunrise,
                sunset = entity.sunset,
                moonrise = entity.moonrise,
                moonset = entity.moonset
            )
        }

    }
}