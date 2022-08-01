package com.desuzed.everyweather.data.mapper

import com.desuzed.everyweather.data.network.dto.weatherApi.HourDto
import com.desuzed.everyweather.domain.model.Hour
import com.desuzed.everyweather.util.EntityMapper

class HourMapper : EntityMapper<HourDto, Hour> {
    override fun mapFromEntity(entity: HourDto): Hour {
        return Hour(
            entity.timeEpoch,
            entity.temp,
            entity.conditionDto?.text.toString(),
            entity.conditionDto?.icon.toString(),
            entity.windSpeed,
            entity.windDegree,
            entity.pressureMb
        )
    }
}