package com.desuzed.everyweather.data.mapper.weather_api

import com.desuzed.everyweather.data.mapper.EntityMapper
import com.desuzed.everyweather.data.network.dto.weatherApi.CurrentDto
import com.desuzed.everyweather.domain.model.weather.Current

class CurrentMapper : EntityMapper<CurrentDto, Current> {
    override fun mapFromEntity(entity: CurrentDto): Current {
        return Current(
            tempC = entity.tempC,
            tempF = entity.tempF,
            text = entity.conditionDto?.text.toString(),
            icon = entity.conditionDto?.icon.toString(),
            windSpeedKph = entity.windSpeedKph,
            windSpeedMph = entity.windSpeedMph,
            pressureMb = entity.pressureMb,
            pressureInch = entity.pressureInch,
            precipMm = entity.precipMm,
            precipInch = entity.precipInch,
            humidity = entity.humidity,
            feelsLikeC = entity.feelsLikeC,
            feelsLikeF = entity.feelsLikeF,
        )
    }
}