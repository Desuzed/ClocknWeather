package com.desuzed.everyweather.data.mapper.weather_api

import com.desuzed.everyweather.data.mapper.EntityMapper
import com.desuzed.everyweather.data.network.dto.weatherApi.WeatherResponseDto
import com.desuzed.everyweather.domain.model.weather.WeatherContent

class WeatherResponseMapper(
    private val locationMapper: LocationMapper,
    private val currentMapper: CurrentMapper,
    private val forecastDayMapper: ForecastDayMapper,
) : EntityMapper<WeatherResponseDto, WeatherContent> {
    override fun mapFromEntity(entity: WeatherResponseDto): WeatherContent {
        val location = locationMapper.mapFromEntity(entity.locationDto!!)
        val current = currentMapper.mapFromEntity(entity.currentDto!!)
        val forecastDayList = entity.forecastDto?.forecastday?.map(forecastDayMapper::mapFromEntity)
        return WeatherContent(
            location = location,
            current = current,
            forecastDay = forecastDayList ?: emptyList()
        )
    }
}