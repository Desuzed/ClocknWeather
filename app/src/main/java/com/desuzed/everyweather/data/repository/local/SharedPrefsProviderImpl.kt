package com.desuzed.everyweather.data.repository.local

import android.content.Context
import com.desuzed.everyweather.domain.model.weather.WeatherResponse
import com.desuzed.everyweather.domain.repository.local.SharedPrefsProvider
import com.google.gson.Gson

class SharedPrefsProviderImpl(context: Context) : SharedPrefsProvider {
    //todo refactor to datastore
    private val sp =
        context.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE)

    override fun saveForecastToCache(weatherResponse: WeatherResponse) {
        val gson = Gson().toJson(weatherResponse)
        sp
            .edit()
            .putString(WEATHER_API, gson)
            .apply()
    }

    override fun loadForecastFromCache(): WeatherResponse? {
        val savedForecast = sp.getString(WEATHER_API, null)
        return Gson().fromJson(savedForecast, WeatherResponse::class.java)
    }

    override fun saveQuery(query: String) {
        sp
            .edit()
            .putString(QUERY, query)
            .apply()
    }

    override fun loadQuery(): String = sp.getString(QUERY, "").toString()

    override fun isFirstRunApp(): Boolean {
        val isFirstTime = sp.getBoolean(IS_FIRST_RUN_APP_KEY, true)
        if (isFirstTime) {
            sp
                .edit()
                .putBoolean(IS_FIRST_RUN_APP_KEY, false)
                .apply()
        }
        return isFirstTime
    }

    companion object {
        const val S_PREF_NAME = "SP"
        const val WEATHER_API = "WEATHER_API"
        const val QUERY = "QUERY"
        private const val IS_FIRST_RUN_APP_KEY = "IS_FIRST_RUN_APP"

    }
}