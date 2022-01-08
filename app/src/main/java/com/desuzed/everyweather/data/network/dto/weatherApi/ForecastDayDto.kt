package com.desuzed.everyweather.data.network.dto.weatherApi

import com.desuzed.everyweather.data.network.dto.EntityMapper
import com.desuzed.everyweather.model.model.ForecastDay
import com.desuzed.everyweather.model.model.Hour
import com.google.gson.annotations.SerializedName

class ForecastDayDto {
    @SerializedName("date")
    var date: String = ""

    @SerializedName("date_epoch")
    var dateEpoch: Long = 0

    @SerializedName("day")
    var day: DayDto? = null

    @SerializedName("astro")
    var astroDto: AstroDto? = null

    @SerializedName("hour")
    var hourDto: ArrayList<HourDto>? = null
}

class ForecastDayMapper : EntityMapper<ForecastDayDto, ForecastDay> {
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