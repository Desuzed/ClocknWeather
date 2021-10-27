package com.desuzed.clocknweather.mvvm.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.desuzed.clocknweather.App
import com.desuzed.clocknweather.mvvm.NetworkLiveData
import com.desuzed.clocknweather.network.dto.WeatherApi
import com.desuzed.clocknweather.network.WeatherApiService
import com.desuzed.clocknweather.network.dto.LocationDto
import com.google.gson.Gson
import kotlinx.coroutines.*
import retrofit2.Response

class NetworkRepository(val app: App) {
    private val networkLiveData= NetworkLiveData (app.applicationContext)

    fun getNetworkLiveData (): NetworkLiveData {
        return networkLiveData
    }
    fun saveForecast(weatherApi: WeatherApi?) {
        val gson = Gson().toJson(weatherApi)
        app.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(WEATHER_API, gson)
            .apply()
    }

    fun loadForecast(): WeatherApi? {
        val savedForecast = app.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE)
            .getString(WEATHER_API, null)
        return Gson().fromJson(savedForecast, WeatherApi::class.java)
//        return when {
//            savedForecast.equals(NO_VALUE) -> {
//                null
//            }
//            else -> {
//
//            }
//        }
    }

    fun saveQuery(query: String) {
        app.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(QUERY, query)
            .apply()
    }

    fun loadQuery(): String? {
        return app.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE)
            .getString(QUERY, DEF_QUERY_LONDON)
    }

    suspend fun getCity(query: String) {
        val b = CoroutineScope(Dispatchers.Main).async {

        }.await()
    }

    suspend fun getForecast(query: String): Response<WeatherApi> {
       return WeatherApiService
            .getInstance()
            .getForecast(query)
    }

    suspend fun getLocation(query: String): Response<LocationDto> {
        return WeatherApiService
            .getInstance()
            .getLocation(query)
    }




//    fun saveLastLocation(lastLocation: LatLon) {
//        app.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE)
//            .edit()
//            .putFloat(LAT, lastLocation.lat.toFloat())
//            .putFloat(LON, lastLocation.lon.toFloat())
//            .apply()
//    }
//
//    fun loadLastLocation(): LatLon {
//        val lat = app.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE)
//            .getFloat(LAT, DEF_LAT_MSK)
//        val lon = app.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE)
//            .getFloat(LON, DEF_LON_MSK)
//        return LatLon(lat.toDouble(), lon.toDouble())
//    }


    companion object {
        const val S_PREF_NAME = "SP"
        const val WEATHER_API = "WEATHER_API"
        const val QUERY = "QUERY"
        const val NO_VALUE = "NO_VALUE"
        const val DEF_QUERY_LONDON = "London"

//        @Volatile
//        private var instance: NetworkRepository? = null
//
//        @Synchronized
//        fun getInstance(app: Application): NetworkRepository =
//            instance ?: NetworkRepository(app).also { instance = it }
    }
}