package com.desuzed.everyweather.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

@Database(entities = [FavoriteLocationDto::class], version = 1, exportSchema = false)
abstract class RoomDbApp : RoomDatabase () {

    abstract fun favoriteLocationDAO(): FavoriteLocationDAO

    companion object {
        @Volatile
        private var INSTANCE: RoomDbApp? = null

        fun getDatabase(context: Context, scope: CoroutineScope): RoomDbApp {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDbApp::class.java,
                    "room_app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}