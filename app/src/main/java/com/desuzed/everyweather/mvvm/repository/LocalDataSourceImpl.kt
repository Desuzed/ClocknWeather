package com.desuzed.everyweather.mvvm.repository

import android.content.Context
import androidx.annotation.WorkerThread
import com.desuzed.everyweather.App
import com.desuzed.everyweather.mvvm.NetworkLiveData
import com.desuzed.everyweather.mvvm.model.WeatherResponse
import com.desuzed.everyweather.mvvm.room.model.FavoriteLocationDto
import com.desuzed.everyweather.mvvm.room.FavoriteLocationDAO
import com.desuzed.everyweather.network.ErrorHandler
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImpl(private val favoriteLocationDAO: FavoriteLocationDAO, val app: App) : LocalDataSource{
    private val sp = app.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE)
    val allLocations: Flow<List<FavoriteLocationDto>> =
        favoriteLocationDAO.getAlphabetizedLocations()

    @WorkerThread
    override suspend fun insert(favoriteLocationDto: FavoriteLocationDto) {
        favoriteLocationDAO.insert(favoriteLocationDto)
    }

    @WorkerThread
    override suspend fun deleteItem(favoriteLocationDto: FavoriteLocationDto) {
        favoriteLocationDAO.deleteItem(favoriteLocationDto)
    }

    @WorkerThread
    override suspend fun containsPrimaryKey(latLon: String): Boolean {
        return favoriteLocationDAO.containsPrimaryKey(latLon)
    }

    override fun saveForecast(weatherResponse: WeatherResponse) {
        val gson = Gson().toJson(weatherResponse)
        sp
            .edit()
            .putString(WEATHER_API, gson)
            .apply()
    }

    override fun loadForecast(): WeatherResponse? {
        val savedForecast =
            sp
                .getString(WEATHER_API, null)
        return Gson().fromJson(savedForecast, WeatherResponse::class.java)
    }

    override fun saveQuery(query: String) {
        app.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(QUERY, query)
            .apply()

//        val gson = Gson().toJson(query)
//        sp
//            .edit()
//            .putString(QUERY, gson)
//            .apply()
    }

    override fun loadQuery(): String  = sp.getString(QUERY, "").toString()


//        val savedQuery = sp.getString(QUERY, "")
//        return Gson().fromJson(savedQuery, Query::class.java) ?: Query("")



    private val networkLiveData = NetworkLiveData(app.applicationContext)

    //todo refactor to sharedViewModel
    override fun getNetworkLiveData(): NetworkLiveData {
        return networkLiveData
    }

    private val errorHandler = ErrorHandler(app.applicationContext)

    //TODO refactor errors to one method
    override fun getInternetError(): String =
        errorHandler.getInternetError()

    override fun getUnknownError(): String =
        errorHandler.getUnknownError()

    override fun noDataToLoad(): String =
        errorHandler.noDataToLoad()

    override fun getApiError(errorCode: Int?): String =
        errorHandler.getApiError(errorCode)

    companion object {
        const val S_PREF_NAME = "SP"
        const val WEATHER_API = "WEATHER_API"
        const val QUERY = "QUERY"
    }

}
