package com.desuzed.everyweather.mvvm.repository

import com.desuzed.everyweather.network.retrofit.NetworkResponse
import com.desuzed.everyweather.network.retrofit.WeatherApiService
import com.desuzed.everyweather.network.dto.ApiErrorDto
import com.desuzed.everyweather.network.dto.WeatherResponseDto

class RemoteDataSourceImpl : RemoteDataSource {
    /**
     * Default language: English
     */
    var lang : String = "en"
    override suspend fun getForecast(query: String): NetworkResponse<WeatherResponseDto, ApiErrorDto> {
        return WeatherApiService
            .getInstance()
            .getForecast(query, lang)
    }

    /**
     * Here will be calls from other weather APIs
     */
}