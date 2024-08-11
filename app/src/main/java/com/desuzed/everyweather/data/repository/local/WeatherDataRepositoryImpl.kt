package com.desuzed.everyweather.data.repository.local

import com.desuzed.everyweather.domain.model.location.UserLatLng
import com.desuzed.everyweather.domain.model.weather.WeatherContent
import com.desuzed.everyweather.domain.model.weather.WeatherQuery
import com.desuzed.everyweather.domain.model.weather.WeatherUniqueContent
import com.desuzed.everyweather.domain.repository.local.SharedPrefsProvider
import com.desuzed.everyweather.domain.repository.local.WeatherDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID

class WeatherDataRepositoryImpl(
    private val weatherDataStore: WeatherDataStore,
    private val sharedPrefsProvider: SharedPrefsProvider,
) : WeatherDataRepository {

    //TODO: Рефакторинг UUID на мультиплатформу
    override suspend fun saveForecastToCache(weatherContent: WeatherContent) {
        try {
            val stringData = Json.encodeToString(
                WeatherUniqueContent(
                    id = UUID.randomUUID().toString(),
                    content = weatherContent,
                )
            )
            weatherDataStore.setForecastData(stringData)

        } catch (_: Exception) {
        }
    }

    //TODO: Удалить после полной раскатки с датастором
    override fun getWeatherContentFlow(): Flow<WeatherContent?> = weatherDataStore
        .getForecastData()
        .map { stringWeather ->
            if (stringWeather.isBlank()) {
                val prefsData = sharedPrefsProvider.loadForecastFromCache()
                if (prefsData != null) {
                    WeatherUniqueContent(
                        id = UUID.randomUUID().toString(),
                        content = prefsData,
                    )
                } else null
            } else {
                stringWeather.decodeFromJson<WeatherUniqueContent>()
            }
        }
        .distinctUntilChangedBy { it?.id }
        .map { it?.content }

    //TODO: Рефакторинг UUID на мультиплатформу
    override suspend fun saveQuery(
        query: String,
        shouldTriggerWeatherRequest: Boolean,
        userLatLng: UserLatLng?,
    ) {
        val stringData = Json.encodeToString(
            WeatherQuery(
                id = UUID.randomUUID().toString(),
                query = query,
                shouldTriggerWeatherRequest = shouldTriggerWeatherRequest,
                userChosenMapPoint = userLatLng,
            )
        )

        weatherDataStore.setQuery(stringData)
    }

    override suspend fun updateQueryShouldNotBeTriggered() {
        val weatherQuery = getQueryFlow().firstOrNull()

        weatherQuery?.let {
            val updatedQuery = weatherQuery.copy(shouldTriggerWeatherRequest = false)
            weatherDataStore.setQuery(Json.encodeToString(updatedQuery))
        }
    }

    //TODO: Удалить после полной раскатки с датастором
    override fun getQueryFlow(): Flow<WeatherQuery> = weatherDataStore
        .getQuery()
        .map { query ->
            val decodedQuery = query.decodeFromJson<WeatherQuery>()
            if (decodedQuery == null || decodedQuery.query.isBlank()) {
                WeatherQuery(
                    id = UUID.randomUUID().toString(),
                    query = sharedPrefsProvider.loadQuery(),
                    userChosenMapPoint = null,
                )
            } else {
                decodedQuery
            }
        }
        .distinctUntilChangedBy { it.id }

    private inline fun <reified T> String.decodeFromJson(): T? {
        if (this.isBlank()) return null
        return try {
            Json.decodeFromString<T>(this)
        } catch (e: Exception) {
            null
        }
    }
}