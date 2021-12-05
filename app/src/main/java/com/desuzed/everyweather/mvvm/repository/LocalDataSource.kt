package com.desuzed.everyweather.mvvm.repository

import com.desuzed.everyweather.mvvm.NetworkLiveData
import com.desuzed.everyweather.mvvm.model.WeatherResponse
import com.desuzed.everyweather.mvvm.room.model.FavoriteLocationDto

interface LocalDataSource {
    suspend fun insert(favoriteLocationDto: FavoriteLocationDto): Long
    suspend fun deleteItem(favoriteLocationDto: FavoriteLocationDto) : Int
    suspend fun containsPrimaryKey(latLon: String): Boolean
    fun saveForecast(weatherResponse: WeatherResponse)
    fun loadForecast(): WeatherResponse?
    fun saveQuery(query: String)
    fun loadQuery(): String
    fun getNetworkLiveData(): NetworkLiveData
    fun getInternetError(): String
    fun getUnknownError(): String
    fun noDataToLoad(): String
    fun getApiError(errorCode: Int?): String
    fun getInsertInfo(saved : Boolean): Pair <Int, String>
    fun getDeleteInfo(deleted : Boolean): Pair <Int, String>
}