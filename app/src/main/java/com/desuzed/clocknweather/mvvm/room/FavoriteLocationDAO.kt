package com.desuzed.clocknweather.mvvm.room

import androidx.room.*
import com.desuzed.clocknweather.mvvm.room.model.FavoriteLocationDto
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteLocationDAO {

    @Query("SELECT * FROM favorite_location_table ORDER BY name ASC")
    fun getAlphabetizedLocations(): Flow<List<FavoriteLocationDto>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favoriteLocationDto: FavoriteLocationDto)

    @Delete
    suspend fun deleteItem(favoriteLocationDto: FavoriteLocationDto)

    @Query("DELETE FROM favorite_location_table")
    suspend fun deleteAll()

    @Query("SELECT count(*)!=0 FROM favorite_location_table WHERE lat_lon = :latLon ")
    suspend fun containsPrimaryKey(latLon: String): Boolean
}