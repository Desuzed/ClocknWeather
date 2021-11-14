package com.desuzed.everyweather.util.mappers

import com.desuzed.everyweather.mvvm.model.Current
import com.desuzed.everyweather.network.dto.CurrentDto

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