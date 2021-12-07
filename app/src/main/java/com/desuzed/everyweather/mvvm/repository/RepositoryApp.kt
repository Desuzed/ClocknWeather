package com.desuzed.everyweather.mvvm.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.desuzed.everyweather.mvvm.model.WeatherResponse
import com.desuzed.everyweather.mvvm.room.model.FavoriteLocationDto
import com.desuzed.everyweather.network.dto.ApiErrorDto
import com.desuzed.everyweather.network.dto.WeatherResponseDto
import com.desuzed.everyweather.network.retrofit.NetworkResponse
import com.desuzed.everyweather.mvvm.NetworkLiveData as NetworkLiveData1

class RepositoryApp(
    private val localDataSource: LocalDataSourceImpl,
    private val remoteDataSource: RemoteDataSourceImpl
) {
    suspend fun insert(favoriteLocationDto: FavoriteLocationDto): Pair <Int, String> {
        val inserted = localDataSource.insert(favoriteLocationDto)
        return localDataSource.getInsertInfo(inserted>0)
    }

    suspend fun deleteItem(favoriteLocationDto: FavoriteLocationDto) : Pair <Int, String> {
        val deleted = localDataSource.deleteItem(favoriteLocationDto)
        return localDataSource.getDeleteInfo(deleted>0)
    }

    suspend fun containsPrimaryKey(latLon: String): Boolean =
        localDataSource.containsPrimaryKey(latLon)

    fun getAllLocations(): LiveData<List<FavoriteLocationDto>> =
        localDataSource.allLocations.asLiveData()

    suspend fun getForecast(query: String): NetworkResponse<WeatherResponseDto, ApiErrorDto> {
        localDataSource.saveQuery(query)
        return remoteDataSource.getForecast(query)
    }

    fun saveForecast(weatherResponse: WeatherResponse) =
        localDataSource.saveForecast(weatherResponse)

    fun loadForecast(): WeatherResponse? = localDataSource.loadForecast()

    fun getInternetError(): String = localDataSource.getInternetError()

    fun getUnknownError(): String = localDataSource.getUnknownError()

    fun noDataToLoad(): String = localDataSource.noDataToLoad()

    fun getApiError(errorCode: Int?): String = localDataSource.getApiError(errorCode)

    fun loadQuery(): String = localDataSource.loadQuery()

    fun getNetworkLiveData(): NetworkLiveData1 = localDataSource.getNetworkLiveData()
}