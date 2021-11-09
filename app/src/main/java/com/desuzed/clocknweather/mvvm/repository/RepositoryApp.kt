package com.desuzed.clocknweather.mvvm.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.desuzed.clocknweather.mvvm.NetworkLiveData
import com.desuzed.clocknweather.mvvm.room.model.FavoriteLocationDto
import com.desuzed.clocknweather.network.dto.ApiError
import com.desuzed.clocknweather.network.dto.WeatherApi
import com.desuzed.clocknweather.network.model.Query
import com.desuzed.clocknweather.network.retrofit.NetworkResponse

class RepositoryApp(val localDataSource: LocalDataSourceImpl, val remoteDataSource: RemoteDataSourceImpl) {

    suspend fun insert(favoriteLocationDto: FavoriteLocationDto) {
        localDataSource.insert(favoriteLocationDto)
    }

    suspend fun deleteItem(favoriteLocationDto: FavoriteLocationDto) {
        localDataSource.deleteItem(favoriteLocationDto)
    }

    suspend fun containsPrimaryKey(latLon: String): Boolean {
        return localDataSource.containsPrimaryKey(latLon)
    }

    fun getAllLocations(): LiveData<List<FavoriteLocationDto>> {
        return localDataSource.allLocations.asLiveData()
    }

    suspend fun getForecast(query: Query): NetworkResponse<WeatherApi, ApiError> {
        localDataSource.saveQuery(query)
        return remoteDataSource.getForecast(query)
    }

    fun saveForecast(weatherApi: WeatherApi?) {
        localDataSource.saveForecast(weatherApi)
    }

    fun loadForecast(): WeatherApi? {
        return localDataSource.loadForecast()
    }

    fun getInternetError(): String {
        return localDataSource.getInternetError()
    }

    fun getUnknownError(): String {
        return localDataSource.getUnknownError()
    }

    fun noDataToLoad(): String {
        return localDataSource.noDataToLoad()
    }

    fun getApiError(errorCode: Int?): String {
        return localDataSource.getApiError(errorCode)
    }

    fun loadQuery(): Query {
        return localDataSource.loadQuery()
    }

    fun getNetworkLiveData(): NetworkLiveData {
        return localDataSource.getNetworkLiveData()
    }
}