package com.desuzed.everyweather.presentation.features.weather_main

import com.desuzed.everyweather.data.mapper.ApiErrorMapper
import com.desuzed.everyweather.data.mapper.WeatherResponseMapper
import com.desuzed.everyweather.data.network.dto.weatherApi.ErrorDtoWeatherApi
import com.desuzed.everyweather.data.network.dto.weatherApi.WeatherResponseDto
import com.desuzed.everyweather.data.network.retrofit.NetworkResponse
import com.desuzed.everyweather.data.repository.providers.action_result.ActionResultProvider
import com.desuzed.everyweather.data.repository.providers.action_result.ActionType
import com.desuzed.everyweather.data.repository.providers.action_result.QueryResult
import com.desuzed.everyweather.domain.model.weather.ResultForecast
import com.desuzed.everyweather.domain.repository.local.SharedPrefsProvider
import com.desuzed.everyweather.domain.repository.remote.RemoteDataSource

class WeatherRepository(
    private val sharedPrefsProvider: SharedPrefsProvider,
    private val remoteDataSource: RemoteDataSource,
    private val weatherResponseMapper: WeatherResponseMapper,
    private val apiErrorMapper: ApiErrorMapper,
) {

    suspend fun fetchForecastOrErrorMessage(query: String, lang: String): ResultForecast {
        if (query.isEmpty()) {
            return ResultForecast(
                weatherResponse = null,
                queryResult = QueryResult(code = ActionResultProvider.NO_DATA, query = query),
            )
        }
        return when (val response = getForecast(query, lang)) {
            is NetworkResponse.Success -> {
                val weatherResponse = weatherResponseMapper.mapFromEntity(response.body)
                sharedPrefsProvider.saveForecastToCache(weatherResponse)
                ResultForecast(weatherResponse, null)
            }
            is NetworkResponse.ApiError -> {
                val apiError = apiErrorMapper.mapFromEntity(response.body)
                ResultForecast(
                    weatherResponse = sharedPrefsProvider.loadForecastFromCache(),
                    queryResult = QueryResult(
                        code = apiError.error.code,
                        query = query,
                        actionType = ActionType.RETRY
                    ),
                )
            }
            is NetworkResponse.NetworkError -> ResultForecast(
                weatherResponse = sharedPrefsProvider.loadForecastFromCache(),
                queryResult = QueryResult(
                    code = ActionResultProvider.NO_INTERNET,
                    query = query,
                    actionType = ActionType.RETRY
                ),
            )
            is NetworkResponse.UnknownError -> ResultForecast(
                weatherResponse = sharedPrefsProvider.loadForecastFromCache(),
                queryResult = QueryResult(
                    code = ActionResultProvider.UNKNOWN,
                    query = query,
                    actionType = ActionType.RETRY
                ),
            )
        }
    }

    private suspend fun getForecast(
        query: String,
        lang: String
    ): NetworkResponse<WeatherResponseDto, ErrorDtoWeatherApi> {
        sharedPrefsProvider.saveQuery(query)
        return remoteDataSource.getForecast(query, lang.lowercase())
    }
}