package com.desuzed.everyweather.util.mappers

import com.desuzed.everyweather.mvvm.model.ForecastDay
import com.desuzed.everyweather.mvvm.model.Hour
import com.desuzed.everyweather.network.dto.ForecastDayDto

class ForecastDayMapper : EntityMapper <ForecastDayDto, ForecastDay> {
    override fun mapFromEntity(entity: ForecastDayDto): ForecastDay {
        val listHour = ArrayList <Hour> ()
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