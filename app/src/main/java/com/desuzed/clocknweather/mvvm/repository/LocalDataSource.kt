package com.desuzed.clocknweather.mvvm.repository

import com.desuzed.clocknweather.mvvm.NetworkLiveData
import com.desuzed.clocknweather.mvvm.room.model.FavoriteLocationDto
import com.desuzed.clocknweather.network.dto.WeatherApi
import com.desuzed.clocknweather.network.model.Query

interface LocalDataSource {
    suspend fun insert(favoriteLocationDto: FavoriteLocationDto)
    suspend fun deleteItem(favoriteLocationDto: FavoriteLocationDto)
    suspend fun containsPrimaryKey(latLon: String): Boolean
    fun saveForecast(weatherApi: WeatherApi?)
    fun loadForecast(): WeatherApi?
    fun saveQuery(query: Query)
    fun loadQuery(): Query
    fun getNetworkLiveData(): NetworkLiveData
    fun getInternetError(): String
    fun getUnknownError(): String
    fun noDataToLoad(): String
    fun getApiError(errorCode: Int?): String
}