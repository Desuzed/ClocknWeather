package com.desuzed.clocknweather.mvvm.repository

import android.content.Context
import com.desuzed.clocknweather.App
import com.desuzed.clocknweather.mvvm.NetworkLiveData
import com.desuzed.clocknweather.network.ErrorHandler
import com.desuzed.clocknweather.network.retrofit.NetworkResponse
import com.desuzed.clocknweather.network.retrofit.WeatherApiService
import com.desuzed.clocknweather.network.dto.ApiError
import com.desuzed.clocknweather.network.dto.LocationDto
import com.desuzed.clocknweather.network.dto.WeatherApi
import com.desuzed.clocknweather.network.model.Query
import com.google.gson.Gson
import retrofit2.Response
//TODO refactor repos to LocalDataSource and RemoteDataSource
class NetworkRepository(val app: App) {
    private val networkLiveData = NetworkLiveData(app.applicationContext)
    private val errorHandler = ErrorHandler(app.applicationContext)
    val sp = app.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE)

    //todo refactor to sharedViewModel
    fun getNetworkLiveData(): NetworkLiveData {
        return networkLiveData
    }
//TODO refactor errors to one method
    fun getInternetError(): String =
        errorHandler.getInternetError()

    fun getUnknownError(): String =
        errorHandler.getUnknownError()

    fun noDataToLoad(): String =
        errorHandler.noDataToLoad()

    fun getApiError(errorCode: Int?): String =
        errorHandler.getApiError(errorCode)


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
    }

    fun saveQuery(query: Query) {
        val gson = Gson().toJson(query)
        sp
            .edit()
            .putString(QUERY, gson)
            .apply()
    }

    fun loadQuery(): Query {
        val savedQuery = sp.getString(QUERY, "")
        return Gson().fromJson(savedQuery, Query::class.java) ?: Query ("")

//        return app.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE)
//            .getString(QUERY, "").toString()
    }


//    suspend fun getForecast(query: String): Response<WeatherApi> {
//        return WeatherApiService
//            .getInstance()
//            .getForecast(query)
//    }

    suspend fun getForecast(query: Query): NetworkResponse<WeatherApi, ApiError> {
        saveQuery(query)
        return WeatherApiService
            .getInstance()
            .getForecast(query.text)
    }


    suspend fun getLocation(query: String): Response<LocationDto> {
        return WeatherApiService
            .getInstance()
            .getLocation(query)
    }


    companion object {
        const val S_PREF_NAME = "SP"
        const val WEATHER_API = "WEATHER_API"
        const val QUERY = "QUERY"
        const val NO_VALUE = "NO_VALUE"
        const val DEF_QUERY_LONDON = "London"

    }
}