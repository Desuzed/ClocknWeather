package com.desuzed.everyweather.data.repository.weather

import com.desuzed.everyweather.data.mapper.weather_api.ApiErrorMapper
import com.desuzed.everyweather.data.mapper.weather_api.WeatherResponseMapper
import com.desuzed.everyweather.data.network.dto.weatherApi.ErrorDtoWeatherApi
import com.desuzed.everyweather.data.network.dto.weatherApi.WeatherResponseDto
import com.desuzed.everyweather.domain.model.location.UserLatLng
import com.desuzed.everyweather.domain.model.network.NetworkResponse
import com.desuzed.everyweather.domain.model.result.ActionType
import com.desuzed.everyweather.domain.model.result.QueryResult
import com.desuzed.everyweather.domain.model.weather.ResultForecast
import com.desuzed.everyweather.domain.model.weather.WeatherContent
import com.desuzed.everyweather.domain.repository.local.SharedPrefsProvider
import com.desuzed.everyweather.domain.repository.provider.ActionResultProvider
import com.desuzed.everyweather.domain.repository.remote.RemoteDataSource

class WeatherRepository(
    private val sharedPrefsProvider: SharedPrefsProvider,
    private val remoteDataSource: RemoteDataSource,
    private val weatherResponseMapper: WeatherResponseMapper,
    private val apiErrorMapper: ApiErrorMapper,
) {

    suspend fun fetchForecastOrErrorMessage(
        query: String,
        lang: String,
        userLatLng: UserLatLng? = null,
    ): ResultForecast {
        if (query.isEmpty()) {
            return ResultForecast(
                weatherContent = null,
                queryResult = QueryResult(code = ActionResultProvider.NO_DATA, query = query),
            )
        }
        return when (val response = getForecast(query, lang)) {
            is NetworkResponse.Success -> {
                val weatherResponse = weatherResponseMapper.mapFromEntity(response.body)
                val resultContent = handleLatLonAndSaveForecast(weatherResponse, userLatLng, query)
                ResultForecast(resultContent, null)
            }
            is NetworkResponse.ApiError -> {
                val apiError = apiErrorMapper.mapFromEntity(response.body)
                ResultForecast(
                    weatherContent = sharedPrefsProvider.loadForecastFromCache(),
                    queryResult = QueryResult(
                        code = apiError.error.code,
                        query = query,
                        actionType = ActionType.RETRY
                    ),
                )
            }
            is NetworkResponse.NetworkError -> ResultForecast(
                weatherContent = sharedPrefsProvider.loadForecastFromCache(),
                queryResult = QueryResult(
                    code = ActionResultProvider.NO_INTERNET,
                    query = query,
                    actionType = ActionType.RETRY
                ),
            )
            is NetworkResponse.UnknownError -> ResultForecast(
                weatherContent = sharedPrefsProvider.loadForecastFromCache(),
                queryResult = QueryResult(
                    code = ActionResultProvider.UNKNOWN,
                    query = query,
                    actionType = ActionType.RETRY
                ),
            )
        }
    }

    private fun handleLatLonAndSaveForecast(
        weatherContent: WeatherContent,
        userLatLng: UserLatLng?,
        query: String,
    ): WeatherContent {
        val latStr = query.substringBefore(",").trim()
        val lonStr = query.substringAfter(",").trim()

        val resultContent = if (userLatLng != null) {
            weatherContent.copy(
                location = weatherContent.location.copy(
                    lat = userLatLng.lat,
                    lon = userLatLng.lon,
                )
            )
        } else try {
            val lat = latStr.toDouble()
            val lon = lonStr.toDouble()
            weatherContent.copy(
                location = weatherContent.location.copy(
                    lat = lat,
                    lon = lon,
                )
            )
        } catch (e: Exception) {
            weatherContent
        }
        sharedPrefsProvider.saveForecastToCache(resultContent)
        return resultContent
    }

    private suspend fun getForecast(
        query: String,
        lang: String,
    ): NetworkResponse<WeatherResponseDto, ErrorDtoWeatherApi> {
        sharedPrefsProvider.saveQuery(query)
        return remoteDataSource.getForecast(query, lang.lowercase())
    }
}