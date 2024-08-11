package com.desuzed.everyweather.domain.repository.local

import com.desuzed.everyweather.domain.model.location.UserLatLng
import com.desuzed.everyweather.domain.model.weather.WeatherContent
import com.desuzed.everyweather.domain.model.weather.WeatherQuery
import kotlinx.coroutines.flow.Flow

interface WeatherDataRepository {
    suspend fun saveForecastToCache(weatherContent: WeatherContent)
    fun getWeatherContentFlow(): Flow<WeatherContent?>
    suspend fun saveQuery(
        query: String,
        shouldTriggerWeatherRequest: Boolean,
        userLatLng: UserLatLng? = null,
    )

    suspend fun updateQueryShouldNotBeTriggered()

    fun getQueryFlow(): Flow<WeatherQuery>
}