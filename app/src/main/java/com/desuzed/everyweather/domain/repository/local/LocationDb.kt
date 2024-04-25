package com.desuzed.everyweather.domain.repository.local

import com.desuzed.everyweather.domain.model.location.FavoriteLocation
import com.desuzed.everyweather.domain.model.weather.Location
import kotlinx.coroutines.flow.Flow

interface LocationDb {
    suspend fun insert(location: Location): Boolean
    suspend fun deleteItem(favoriteLocationDto: FavoriteLocation): Boolean
    suspend fun updateLocation(favoriteLocationDto: FavoriteLocation): Boolean
    suspend fun containsPrimaryKey(latLon: String): Boolean
    fun getAllLocations(): Flow<List<FavoriteLocation>>
}