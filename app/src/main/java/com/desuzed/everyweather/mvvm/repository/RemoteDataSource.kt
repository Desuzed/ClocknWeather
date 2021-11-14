package com.desuzed.everyweather.mvvm.repository

import com.desuzed.everyweather.network.dto.ApiErrorDto
import com.desuzed.everyweather.network.dto.WeatherResponseDto
import com.desuzed.everyweather.network.retrofit.NetworkResponse

interface RemoteDataSource {
    suspend fun getForecast(query: String): NetworkResponse<WeatherResponseDto, ApiErrorDto>
}