package com.desuzed.clocknweather.mvvm.repository

import android.content.Context
import com.desuzed.clocknweather.App
import com.desuzed.clocknweather.mvvm.NetworkLiveData
import com.desuzed.clocknweather.network.ErrorHandler
import com.desuzed.clocknweather.network.retrofit.NetworkResponse
import com.desuzed.clocknweather.network.retrofit.WeatherApiService
import com.desuzed.clocknweather.network.dto.ApiError
import com.desuzed.clocknweather.network.dto.LocationDto
import com.desuzed.clocknweather.network.dto.WeatherApi
import com.desuzed.clocknweather.network.model.Query
import com.google.gson.Gson
import retrofit2.Response
//TODO refactor repos to LocalDataSource and RemoteDataSource
class RemoteDataSourceImpl : RemoteDataSource {
    override suspend fun getForecast(query: Query): NetworkResponse<WeatherApi, ApiError> {
        return WeatherApiService
            .getInstance()
            .getForecast(query.text)
    }
}