package com.desuzed.everyweather.data.network.dto.weatherApi.mappers

import com.desuzed.everyweather.model.model.Day
import com.desuzed.everyweather.data.network.dto.weatherApi.DayDto

class DayMapper : EntityMapper<DayDto, Day> {
    override fun mapFromEntity(entity: DayDto): Day {
        return Day(
            entity.maxTemp,
            entity.minTemp,
            entity.maxWind,
            entity.totalPrecip,
            entity.avgHumidity,
            entity.popRain,
            entity.popSnow,
            entity.conditionDto?.text.toString(),
            entity.conditionDto?.icon.toString()
        )
    }
}