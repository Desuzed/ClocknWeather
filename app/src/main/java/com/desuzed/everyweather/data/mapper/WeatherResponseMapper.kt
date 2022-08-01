package com.desuzed.everyweather.data.mapper

import com.desuzed.everyweather.data.network.dto.weatherApi.WeatherResponseDto
import com.desuzed.everyweather.domain.model.WeatherResponse
import com.desuzed.everyweather.util.EntityMapper

object WeatherResponseMapper : EntityMapper<WeatherResponseDto, WeatherResponse> {
    override fun mapFromEntity(entity: WeatherResponseDto): WeatherResponse {
        val location = LocationMapper.mapFromEntity(entity.locationDto!!)
        val current = CurrentMapper.mapFromEntity(entity.currentDto!!)
        val forecastDayList = entity.forecastDto?.forecastday?.map(ForecastDayMapper::mapFromEntity)
        return WeatherResponse(location, current, forecastDayList ?: emptyList())
    }
}