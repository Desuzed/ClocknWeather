package com.desuzed.everyweather.data.network.dto.weatherApi.mappers

import android.annotation.SuppressLint
import com.desuzed.everyweather.model.model.Astro
import com.desuzed.everyweather.data.network.dto.weatherApi.AstroDto
import java.text.SimpleDateFormat

class AstroMapper : EntityMapper<AstroDto, Astro> {
    @SuppressLint("SimpleDateFormat")
    private val apiFormat = SimpleDateFormat("hh:mm a")
    @SuppressLint("SimpleDateFormat")
    private val appFormat = SimpleDateFormat("HH:mm")

    override fun mapFromEntity(entity: AstroDto): Astro {
        //TODO Отловить случаи когда с апи не приходит moonrise/moonset и чтобы не крашнулось добавил костыль
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