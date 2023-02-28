package com.desuzed.everyweather.domain.repository.local

import com.desuzed.everyweather.domain.model.weather.WeatherContent

interface SharedPrefsProvider {
    fun saveForecastToCache(weatherContent: WeatherContent)
    fun loadForecastFromCache(): WeatherContent?
    fun saveQuery(query: String)
    fun loadQuery(): String
    fun isFirstRunApp(): Boolean
}