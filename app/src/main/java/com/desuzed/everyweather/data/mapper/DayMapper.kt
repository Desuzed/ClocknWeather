package com.desuzed.everyweather.data.mapper

import com.desuzed.everyweather.data.network.dto.weatherApi.DayDto
import com.desuzed.everyweather.domain.model.Day
import com.desuzed.everyweather.util.EntityMapper

object DayMapper : EntityMapper<DayDto, Day> {
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