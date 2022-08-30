package com.desuzed.everyweather.data.mapper

import com.desuzed.everyweather.data.network.dto.weatherApi.CurrentDto
import com.desuzed.everyweather.domain.model.Current
import com.desuzed.everyweather.util.EntityMapper

class CurrentMapper : EntityMapper<CurrentDto, Current> {
    override fun mapFromEntity(entity: CurrentDto): Current {
        return Current(
            entity.tempC,
            entity.tempF,
            entity.conditionDto?.text.toString(),
            entity.conditionDto?.icon.toString(),
            entity.windSpeedKph,
            entity.windSpeedMph,
            entity.windDegree,
            entity.windDir,
            entity.pressureMb,
            entity.pressureInch,
            entity.precipMm,
            entity.precipInch,
            entity.humidity,
            entity.feelsLikeC,
            entity.feelsLikeF,
        )
    }
}