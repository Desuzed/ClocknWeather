package com.desuzed.everyweather.data.mapper.weather_api

import com.desuzed.everyweather.data.mapper.EntityMapper
import com.desuzed.everyweather.data.network.dto.weatherApi.DayDto
import com.desuzed.everyweather.domain.model.weather.Day

class DayMapper : EntityMapper<DayDto, Day> {
    override fun mapFromEntity(entity: DayDto): Day {
        return Day(
            maxTempC = entity.maxTempC,
            maxTempF = entity.maxTempF,
            minTempC = entity.minTempC,
            minTempF = entity.minTempF,
            maxWindKph = entity.maxWindKph,
            maxWindMph = entity.maxWindMph,
            totalPrecipMm = entity.totalPrecipMm,
            totalPrecipInch = entity.totalPrecipInch,
            avgHumidity = entity.avgHumidity,
            popRain = entity.popRain,
            text = entity.conditionDto?.text.toString(),
            icon = entity.conditionDto?.icon.toString()
        )
    }
}