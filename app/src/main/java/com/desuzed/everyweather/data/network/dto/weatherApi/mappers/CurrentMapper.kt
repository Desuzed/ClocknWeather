package com.desuzed.everyweather.data.network.dto.weatherApi.mappers

import com.desuzed.everyweather.model.model.Current
import com.desuzed.everyweather.data.network.dto.weatherApi.CurrentDto

class CurrentMapper : EntityMapper<CurrentDto, Current> {
    override fun mapFromEntity(entity: CurrentDto): Current {
        return Current(
            entity.temp,
            entity.conditionDto?.text.toString(),
            entity.conditionDto?.icon.toString(),
            entity.windSpeed,
            entity.windDegree,
            entity.windDir,
            entity.pressureMb,
            entity.precipMm,
            entity.humidity,
            entity.feelsLike
        )
    }
}