package com.desuzed.everyweather.domain.repository.local

import com.desuzed.everyweather.data.room.FavoriteLocationDto
import kotlinx.coroutines.flow.Flow

interface RoomProvider {
    suspend fun insert(favoriteLocationDto: FavoriteLocationDto): Boolean
    suspend fun deleteItem(favoriteLocationDto: FavoriteLocationDto): Boolean
    suspend fun containsPrimaryKey(latLon: String): Boolean
    fun getAllLocations(): Flow<List<FavoriteLocationDto>>
}