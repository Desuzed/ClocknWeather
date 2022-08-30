package com.desuzed.everyweather.data.mapper

import com.desuzed.everyweather.data.network.dto.weatherApi.DayDto
import com.desuzed.everyweather.domain.model.Day
import com.desuzed.everyweather.util.EntityMapper

class DayMapper : EntityMapper<DayDto, Day> {
    override fun mapFromEntity(entity: DayDto): Day {
        return Day(
            entity.maxTempC,
            entity.maxTempF,
            entity.minTempC,
            entity.minTempF,
            entity.maxWindKph,
            entity.maxWindMph,
            entity.totalPrecipMm,
            entity.totalPrecipInch,
            entity.avgHumidity,
            entity.popRain,
            entity.popSnow,
            entity.conditionDto?.text.toString(),
            entity.conditionDto?.icon.toString()
        )
    }
}