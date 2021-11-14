package com.desuzed.everyweather.mvvm.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.desuzed.everyweather.mvvm.NetworkLiveData
import com.desuzed.everyweather.mvvm.model.WeatherResponse
import com.desuzed.everyweather.mvvm.room.model.FavoriteLocationDto
import com.desuzed.everyweather.network.dto.ApiErrorDto
import com.desuzed.everyweather.network.dto.WeatherResponseDto
import com.desuzed.everyweather.network.retrofit.NetworkResponse

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

    suspend fun getForecast(query: String): NetworkResponse<WeatherResponseDto, ApiErrorDto> {
        localDataSource.saveQuery(query)
        return remoteDataSource.getForecast(query)
    }

    fun saveForecast(weatherResponse: WeatherResponse) {
        localDataSource.saveForecast(weatherResponse)
    }

    fun loadForecast(): WeatherResponse? {
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

    fun loadQuery(): String {
        return localDataSource.loadQuery()
    }

    fun getNetworkLiveData(): NetworkLiveData {
        return localDataSource.getNetworkLiveData()
    }
}