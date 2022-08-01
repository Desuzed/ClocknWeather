package com.desuzed.everyweather.data.mapper

import com.desuzed.everyweather.data.network.dto.weatherApi.ForecastDayDto
import com.desuzed.everyweather.domain.model.ForecastDay
import com.desuzed.everyweather.util.EntityMapper

object ForecastDayMapper : EntityMapper<ForecastDayDto, ForecastDay> {
    override fun mapFromEntity(entity: ForecastDayDto): ForecastDay {
        val listHour = entity.hourDto?.map(HourMapper::mapFromEntity)
        return ForecastDay(
            entity.date,
            entity.dateEpoch,
            DayMapper.mapFromEntity(entity.day!!),
            AstroMapper.mapFromEntity(entity.astroDto!!),
            listHour ?: emptyList()
        )
    }
}