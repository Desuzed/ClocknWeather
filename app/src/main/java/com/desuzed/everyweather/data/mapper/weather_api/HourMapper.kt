package com.desuzed.everyweather.data.mapper.weather_api

import com.desuzed.everyweather.data.mapper.EntityMapper
import com.desuzed.everyweather.data.network.dto.weatherApi.HourDto
import com.desuzed.everyweather.domain.model.weather.Hour

class HourMapper : EntityMapper<HourDto, Hour> {
    override fun mapFromEntity(entity: HourDto): Hour {
        return Hour(
            timeEpoch = entity.timeEpoch,
            tempC = entity.tempC,
            tempF = entity.tempF,
            text = entity.conditionDto?.text.toString(),
            icon = entity.conditionDto?.icon.toString(),
            windSpeedKmh = entity.windSpeedKph,
            windSpeedMph = entity.windSpeedMph,
            windDegree = entity.windDegree,
            pressureMb = entity.pressureMb,
            pressureInch = entity.pressureInch,
        )
    }
}