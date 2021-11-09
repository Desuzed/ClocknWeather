package com.desuzed.clocknweather.mvvm.repository

import com.desuzed.clocknweather.network.dto.ApiError
import com.desuzed.clocknweather.network.dto.WeatherApi
import com.desuzed.clocknweather.network.model.Query
import com.desuzed.clocknweather.network.retrofit.NetworkResponse

interface RemoteDataSource {
    suspend fun getForecast(query: Query): NetworkResponse<WeatherApi, ApiError>
}