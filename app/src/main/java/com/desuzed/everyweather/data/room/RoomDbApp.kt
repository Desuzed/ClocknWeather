package com.desuzed.everyweather.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteLocationDto::class], version = 1, exportSchema = false)
abstract class RoomDbApp : RoomDatabase() {

    abstract fun favoriteLocationDAO(): FavoriteLocationDAO

    companion object {
        const val DB_NAME = "room_app_database"
    }
}