package com.desuzed.everyweather.data.network.dto.weatherApi.mappers

import com.desuzed.everyweather.model.model.ForecastDay
import com.desuzed.everyweather.model.model.Hour
import com.desuzed.everyweather.data.network.dto.weatherApi.ForecastDayDto

class ForecastDayMapper : EntityMapper <ForecastDayDto, ForecastDay> {
    override fun mapFromEntity(entity: ForecastDayDto): ForecastDay {
        val listHour = mutableListOf<Hour>()
         entity.hourDto?.forEach {
            listHour.add(HourMapper().mapFromEntity(it))
        }
        return ForecastDay(
            entity.date,
            entity.dateEpoch,
            DayMapper().mapFromEntity(entity.day!!),
            AstroMapper().mapFromEntity(entity.astroDto!!),
            listHour
        )
    }
}