package com.desuzed.everyweather.data.mapper

import com.desuzed.everyweather.data.network.dto.weatherApi.WeatherResponseDto
import com.desuzed.everyweather.domain.model.weather.WeatherResponse
import com.desuzed.everyweather.util.EntityMapper

class WeatherResponseMapper(
    private val locationMapper: LocationMapper,
    private val currentMapper: CurrentMapper,
    private val forecastDayMapper: ForecastDayMapper,
) : EntityMapper<WeatherResponseDto, WeatherResponse> {
    override fun mapFromEntity(entity: WeatherResponseDto): WeatherResponse {
        val location = locationMapper.mapFromEntity(entity.locationDto!!)
        val current = currentMapper.mapFromEntity(entity.currentDto!!)
        val forecastDayList = entity.forecastDto?.forecastday?.map(forecastDayMapper::mapFromEntity)
        return WeatherResponse(location, current, forecastDayList ?: emptyList())
    }
}