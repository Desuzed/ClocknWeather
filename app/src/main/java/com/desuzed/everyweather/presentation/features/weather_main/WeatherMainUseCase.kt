package com.desuzed.everyweather.presentation.features.weather_main

import com.desuzed.everyweather.data.mapper.ApiErrorMapper
import com.desuzed.everyweather.data.mapper.WeatherResponseMapper
import com.desuzed.everyweather.data.network.dto.weatherApi.ErrorDtoWeatherApi
import com.desuzed.everyweather.data.network.dto.weatherApi.WeatherResponseDto
import com.desuzed.everyweather.data.network.retrofit.NetworkResponse
import com.desuzed.everyweather.domain.model.ResultForecast
import com.desuzed.everyweather.domain.model.settings.Language
import com.desuzed.everyweather.domain.repository.local.SharedPrefsProvider
import com.desuzed.everyweather.domain.repository.remote.RemoteDataSource
import com.desuzed.everyweather.util.ActionResultProvider

class WeatherMainUseCase(
    private val sharedPrefsProvider: SharedPrefsProvider,
    private val remoteDataSource: RemoteDataSource,
    private val actionResultProvider: ActionResultProvider,
    private val weatherResponseMapper: WeatherResponseMapper,
    private val apiErrorMapper: ApiErrorMapper,
) {

    suspend fun fetchForecastOrErrorMessage(query: String, lang: Language): ResultForecast {
        if (query.isEmpty()) {
            return ResultForecast(
                null,
                actionResultProvider.parseCode(ActionResultProvider.NO_DATA)
            )
        }
        return when (val response = getForecast(query, lang.lang)) {
            is NetworkResponse.Success -> {
                val weatherResponse = weatherResponseMapper.mapFromEntity(response.body)
                sharedPrefsProvider.saveForecastToCache(weatherResponse)
                ResultForecast(weatherResponse, null)
            }
            is NetworkResponse.ApiError -> {
                val apiError = apiErrorMapper.mapFromEntity(response.body)
                ResultForecast(
                    sharedPrefsProvider.loadForecastFromCache(),
                    actionResultProvider.parseCode(apiError.error.code)
                )
            }
            is NetworkResponse.NetworkError -> ResultForecast(
                sharedPrefsProvider.loadForecastFromCache(),
                actionResultProvider.parseCode(ActionResultProvider.NO_INTERNET)
            )
            is NetworkResponse.UnknownError -> ResultForecast(
                sharedPrefsProvider.loadForecastFromCache(),
                actionResultProvider.parseCode(ActionResultProvider.UNKNOWN)
            )
        }
    }

    private suspend fun getForecast(query: String, lang: String): NetworkResponse<WeatherResponseDto, ErrorDtoWeatherApi> {
        sharedPrefsProvider.saveQuery(query)
        return remoteDataSource.getForecast(query, lang)
    }
}