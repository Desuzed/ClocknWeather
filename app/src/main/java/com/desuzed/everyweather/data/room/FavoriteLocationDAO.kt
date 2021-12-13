package com.desuzed.everyweather.data.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteLocationDAO {

    @Query("SELECT * FROM favorite_location_table ORDER BY name ASC")
    fun getAlphabetizedLocations(): Flow<List<FavoriteLocationDto>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favoriteLocationDto: FavoriteLocationDto) : Long

    @Delete
    suspend fun deleteItem(favoriteLocationDto: FavoriteLocationDto) : Int

    @Query("DELETE FROM favorite_location_table")
    suspend fun deleteAll()

    @Query("SELECT count(*)!=0 FROM favorite_location_table WHERE lat_lon = :latLon ")
    suspend fun containsPrimaryKey(latLon: String): Boolean
}