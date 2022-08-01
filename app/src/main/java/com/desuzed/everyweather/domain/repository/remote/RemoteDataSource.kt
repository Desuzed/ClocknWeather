package com.desuzed.everyweather.domain.repository.remote

import com.desuzed.everyweather.data.network.dto.weatherApi.ErrorDtoWeatherApi
import com.desuzed.everyweather.data.network.dto.weatherApi.WeatherResponseDto
import com.desuzed.everyweather.data.network.retrofit.NetworkResponse

interface RemoteDataSource {
    suspend fun getForecast(query: String): NetworkResponse<WeatherResponseDto, ErrorDtoWeatherApi>
}