package com.desuzed.everyweather.util.mappers

import com.desuzed.everyweather.mvvm.model.Hour
import com.desuzed.everyweather.network.dto.HourDto

class HourMapper: EntityMapper <HourDto, Hour> {
    override fun mapFromEntity(entity: HourDto): Hour {
        return Hour (entity.timeEpoch,
            entity.temp,
            entity.conditionDto?.text.toString(),
            entity.conditionDto?.icon.toString(),
            entity.windSpeed,
            entity.windDegree,
            entity.pressureMb
        )
    }
}