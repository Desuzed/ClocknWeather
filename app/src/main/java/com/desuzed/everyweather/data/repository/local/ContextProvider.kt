package com.desuzed.everyweather.data.repository.local

import android.content.Context
import com.desuzed.everyweather.data.network.ActionResultProviderImpl
import com.desuzed.everyweather.model.model.WeatherResponse
import com.google.gson.Gson

class ContextProviderImpl(private val context: Context) : ContextProvider {
    private val sp =
        context.getSharedPreferences(LocalDataSourceImpl.S_PREF_NAME, Context.MODE_PRIVATE)

    override fun saveForecastToCache(weatherResponse: WeatherResponse) {
        val gson = Gson().toJson(weatherResponse)
        sp
            .edit()
            .putString(LocalDataSourceImpl.WEATHER_API, gson)
            .apply()
    }

    override fun loadForecastFromCache(): WeatherResponse? {
        val savedForecast = sp.getString(LocalDataSourceImpl.WEATHER_API, null)
        return Gson().fromJson(savedForecast, WeatherResponse::class.java)
    }

    override fun saveQuery(query: String) {
        sp
            .edit()
            .putString(LocalDataSourceImpl.QUERY, query)
            .apply()
    }

    override fun loadQuery(): String = sp.getString(LocalDataSourceImpl.QUERY, "").toString()

    override fun parseCode(errorCode: Int): String =
        ActionResultProviderImpl(context.resources).parseCode(errorCode)
}

interface ContextProvider {
    fun saveForecastToCache(weatherResponse: WeatherResponse)
    fun loadForecastFromCache(): WeatherResponse?
    fun saveQuery(query: String)
    fun loadQuery(): String
    fun parseCode(errorCode: Int): String
}