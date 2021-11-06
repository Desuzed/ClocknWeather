package com.desuzed.clocknweather.mvvm.repository

import androidx.annotation.WorkerThread
import com.desuzed.clocknweather.mvvm.room.model.FavoriteLocationDto
import com.desuzed.clocknweather.mvvm.room.FavoriteLocationDAO
import kotlinx.coroutines.flow.Flow

class FavoriteLocationRepository(private val favoriteLocationDAO: FavoriteLocationDAO) {
    val allLocations: Flow<List<FavoriteLocationDto>> = favoriteLocationDAO.getAlphabetizedLocations()

    @WorkerThread
    suspend fun insert(favoriteLocationDto: FavoriteLocationDto) {
        favoriteLocationDAO.insert(favoriteLocationDto)
    }

    @WorkerThread
    suspend fun deleteItem(favoriteLocationDto: FavoriteLocationDto) {
        favoriteLocationDAO.deleteItem(favoriteLocationDto)
    }

    @WorkerThread
    suspend fun containsPrimaryKey(latLon : String) : Boolean{
        return favoriteLocationDAO.containsPrimaryKey(latLon)
    }
}