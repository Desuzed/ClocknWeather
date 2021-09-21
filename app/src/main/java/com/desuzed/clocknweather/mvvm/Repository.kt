package com.desuzed.clocknweather.mvvm

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.desuzed.clocknweather.retrofit.WeatherApi
import com.desuzed.clocknweather.retrofit.WeatherApiService
import com.desuzed.clocknweather.retrofit.pojo.LatLon
import com.google.gson.Gson
import retrofit2.Response

class Repository(val app: Application) {
    fun saveForecast(weatherApi: WeatherApi, saveTime: Long) {
        val gson = Gson().toJson(weatherApi)
        app.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(WEATHER_API, gson)
            .putLong(SAVED_TIME_KEY, saveTime)
            .apply()
    }

    @SuppressLint("SimpleDateFormat")
    fun loadForecast(): WeatherApi? {
        val savedForecast = app.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE)
            .getString(WEATHER_API, NO_VALUE)
        return when {
            savedForecast.equals(NO_VALUE) -> {
                null
            }
            else -> {
                Gson().fromJson(savedForecast, WeatherApi::class.java)
            }
        }
    }

    suspend fun getForecast(query: String): Response<WeatherApi> {
        return WeatherApiService
            .getInstance()
            .getForecast(query)
    }

    fun saveLastLocation(lastLocation: LatLon) {
        app.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putFloat(LAT, lastLocation.lat.toFloat())
            .putFloat(LON, lastLocation.lon.toFloat())
            .apply()
    }

    fun loadLastLocation(): LatLon {
        val lat = app.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE)
            .getFloat(LAT, DEF_LAT_MSK)
        val lon = app.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE)
            .getFloat(LON, DEF_LON_MSK)
        return LatLon(lat.toDouble(), lon.toDouble())
    }

    companion object {
        const val S_PREF_NAME = "SP"
        const val WEATHER_API = "WEATHER_API"
        const val NO_VALUE = "NO_VALUE"
        const val SAVED_TIME_KEY = "SAVED_TIME_KEY"
        const val LAT = "LAT"
        const val LON = "LON"
        const val DEF_LAT_MSK: Float = 55.751244F
        const val DEF_LON_MSK: Float = 37.618423F
    }
}