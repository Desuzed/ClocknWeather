package com.desuzed.everyweather.data.mapper

import com.desuzed.everyweather.data.network.dto.weatherApi.ForecastDayDto
import com.desuzed.everyweather.domain.model.weather.ForecastDay
import com.desuzed.everyweather.util.EntityMapper

class ForecastDayMapper(
    private val dayMapper: DayMapper,
    private val hourMapper: HourMapper,
    private val astroMapper: AstroMapper,
) : EntityMapper<ForecastDayDto, ForecastDay> {
    override fun mapFromEntity(entity: ForecastDayDto): ForecastDay {
        val listHour = entity.hourDto?.map(hourMapper::mapFromEntity)
        return ForecastDay(
            entity.date,
            entity.dateEpoch,
            dayMapper.mapFromEntity(entity.day!!),
            astroMapper.mapFromEntity(entity.astroDto!!),
            listHour ?: emptyList()
        )
    }
}