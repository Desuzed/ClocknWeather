package com.desuzed.everyweather.data.mapper.weather_api

import com.desuzed.everyweather.data.mapper.EntityMapper
import com.desuzed.everyweather.data.network.dto.weatherApi.ForecastDayDto
import com.desuzed.everyweather.domain.model.weather.ForecastDay

class ForecastDayMapper(
    private val dayMapper: DayMapper,
    private val hourMapper: HourMapper,
    private val astroMapper: AstroMapper,
) : EntityMapper<ForecastDayDto, ForecastDay> {
    override fun mapFromEntity(entity: ForecastDayDto): ForecastDay {
        val listHour = entity.hourDto?.map(hourMapper::mapFromEntity)
        return ForecastDay(
            date = entity.date,
            dateEpoch = entity.dateEpoch,
            day = dayMapper.mapFromEntity(entity.day!!),
            astro = astroMapper.mapFromEntity(entity.astroDto!!),
            hourForecast = listHour ?: emptyList()
        )
    }
}