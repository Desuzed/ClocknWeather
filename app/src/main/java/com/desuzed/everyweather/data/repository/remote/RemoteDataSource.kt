package com.desuzed.everyweather.data.repository.remote

import com.desuzed.everyweather.data.network.dto.weatherApi.ErrorDtoWeatherApi
import com.desuzed.everyweather.data.network.dto.weatherApi.WeatherResponseDto
import com.desuzed.everyweather.data.network.retrofit.NetworkResponse
import com.desuzed.everyweather.data.network.retrofit.WeatherApiService
import com.desuzed.everyweather.domain.repository.remote.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteDataSourceImpl(private val api: WeatherApiService) :
    RemoteDataSource { //todo koin , settings feature
    /**
     * Default language: English
     */
    var lang: String = "en"

    override suspend fun getForecast(query: String): NetworkResponse<WeatherResponseDto, ErrorDtoWeatherApi> =
        withContext(Dispatchers.IO) {
            api.getForecast(query, lang)
        }
}