package com.desuzed.everyweather.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LatLngDAO {

    @Query("SELECT * FROM lat_lng_table")
    fun getLatLng(): Flow<LatLngDto>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(latLng: LatLngDto): Long

}