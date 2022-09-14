package com.desuzed.everyweather.domain.repository.local

import com.desuzed.everyweather.domain.model.weather.WeatherResponse

interface SharedPrefsProvider {
    fun saveForecastToCache(weatherResponse: WeatherResponse)
    fun loadForecastFromCache(): WeatherResponse?
    fun saveQuery(query: String)
    fun loadQuery(): String
    fun isFirstRunApp(): Boolean
}