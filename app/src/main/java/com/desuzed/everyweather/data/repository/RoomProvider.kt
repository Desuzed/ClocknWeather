package com.desuzed.everyweather.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.desuzed.everyweather.data.room.FavoriteLocationDAO
import com.desuzed.everyweather.data.room.FavoriteLocationDto
import com.desuzed.everyweather.data.room.LatLngDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomProviderImpl(
    private val favoriteLocationDAO: FavoriteLocationDAO,
    private val latLngDAO: LatLngDAO
) : RoomProvider {
    override suspend fun insert(favoriteLocationDto: FavoriteLocationDto): Boolean =
        withContext(Dispatchers.IO) {
            val inserted = favoriteLocationDAO.insert(favoriteLocationDto)
            inserted > 0
        }

    override suspend fun deleteItem(favoriteLocationDto: FavoriteLocationDto): Boolean =
        withContext(Dispatchers.IO) {
            val deleted = favoriteLocationDAO.deleteItem(favoriteLocationDto)
            deleted > 0
        }


    override suspend fun containsPrimaryKey(latLon: String): Boolean =
        withContext(Dispatchers.IO) {
            favoriteLocationDAO.containsPrimaryKey(latLon)
        }


    override fun getAllLocations(): LiveData<List<FavoriteLocationDto>> =
        favoriteLocationDAO.getAlphabetizedLocations().asLiveData()
}


interface RoomProvider {
    suspend fun insert(favoriteLocationDto: FavoriteLocationDto): Boolean
    suspend fun deleteItem(favoriteLocationDto: FavoriteLocationDto): Boolean
    suspend fun containsPrimaryKey(latLon: String): Boolean
    fun getAllLocations(): LiveData<List<FavoriteLocationDto>>
}