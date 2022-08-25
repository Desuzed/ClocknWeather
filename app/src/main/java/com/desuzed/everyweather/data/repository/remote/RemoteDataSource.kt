package com.desuzed.everyweather.data.repository.remote

import com.desuzed.everyweather.data.network.dto.weatherApi.ErrorDtoWeatherApi
import com.desuzed.everyweather.data.network.dto.weatherApi.WeatherResponseDto
import com.desuzed.everyweather.data.network.retrofit.NetworkResponse
import com.desuzed.everyweather.data.network.retrofit.WeatherApiService
import com.desuzed.everyweather.domain.repository.remote.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteDataSourceImpl(private val api: WeatherApiService) :
    RemoteDataSource {

    override suspend fun getForecast(
        query: String,
        lang: String
    ): NetworkResponse<WeatherResponseDto, ErrorDtoWeatherApi> =
        withContext(Dispatchers.IO) {
            api.getForecast(query, lang)
        }
}