package com.desuzed.everyweather.data.repository.local

import com.desuzed.everyweather.data.room.FavoriteLocationDAO
import com.desuzed.everyweather.data.room.FavoriteLocationDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class RoomProviderImpl(private val favoriteLocationDAO: FavoriteLocationDAO) : RoomProvider {
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


    override fun getAllLocations(): Flow<List<FavoriteLocationDto>> =
        favoriteLocationDAO.getAlphabetizedLocations()
}


interface RoomProvider {
    suspend fun insert(favoriteLocationDto: FavoriteLocationDto): Boolean
    suspend fun deleteItem(favoriteLocationDto: FavoriteLocationDto): Boolean
    suspend fun containsPrimaryKey(latLon: String): Boolean
    fun getAllLocations(): Flow<List<FavoriteLocationDto>>
}