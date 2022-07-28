package com.desuzed.everyweather.data.network.dto.weatherApi

import com.desuzed.everyweather.domain.model.ForecastDay
import com.desuzed.everyweather.domain.model.WeatherResponse
import com.desuzed.everyweather.util.EntityMapper
import com.google.gson.annotations.SerializedName

class WeatherResponseDto {
    @SerializedName("location")
    var locationDto: LocationDto? = null

    @SerializedName("current")
    var currentDto: CurrentDto? = null

    @SerializedName("forecast")
    var forecastDto: ForecastDto? = null

}

object WeatherResponseMapper : EntityMapper<WeatherResponseDto, WeatherResponse> {
    override fun mapFromEntity(entity: WeatherResponseDto): WeatherResponse {
        val location = LocationMapper().mapFromEntity(entity.locationDto!!)
        val current = CurrentMapper().mapFromEntity(entity.currentDto!!)
        val forecastDayList = mutableListOf<ForecastDay>()
        entity.forecastDto?.forecastday?.forEach {
            forecastDayList.add(ForecastDayMapper().mapFromEntity(it))
        }
        return WeatherResponse(location, current, forecastDayList)
    }
}

