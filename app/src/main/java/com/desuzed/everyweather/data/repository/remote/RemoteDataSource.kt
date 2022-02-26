package com.desuzed.everyweather.data.repository

import com.desuzed.everyweather.data.network.dto.weatherApi.ErrorDtoWeatherApi
import com.desuzed.everyweather.data.network.dto.weatherApi.WeatherResponseDto
import com.desuzed.everyweather.data.network.retrofit.NetworkResponse
import com.desuzed.everyweather.data.network.retrofit.WeatherApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteDataSourceImpl : RemoteDataSource {
    /**
     * Default language: English
     */
    var lang: String = "en"

    override suspend fun getForecast(query: String): NetworkResponse<WeatherResponseDto, ErrorDtoWeatherApi> =
        withContext(Dispatchers.IO) {
            WeatherApiService
                .getInstance()
                .getForecast(query, lang)
        }
}


interface RemoteDataSource {
    suspend fun getForecast(query: String): NetworkResponse<WeatherResponseDto, ErrorDtoWeatherApi>
}