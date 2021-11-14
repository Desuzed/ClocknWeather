package com.desuzed.everyweather.util.mappers

import com.desuzed.everyweather.mvvm.model.ForecastDay
import com.desuzed.everyweather.mvvm.model.WeatherResponse
import com.desuzed.everyweather.network.dto.WeatherResponseDto

class WeatherResponseMapper: EntityMapper<WeatherResponseDto, WeatherResponse> {
    override fun mapFromEntity(entity: WeatherResponseDto): WeatherResponse {
        val location = LocationMapper().mapFromEntity(entity.locationDto!!)
        val current = CurrentMapper().mapFromEntity(entity.currentDto!!)
        val forecastDayList = ArrayList<ForecastDay>()
        entity.forecastDto?.forecastday?.forEach {
            forecastDayList.add(ForecastDayMapper().mapFromEntity(it))
        }
        return WeatherResponse(location, current, forecastDayList)
    }
}