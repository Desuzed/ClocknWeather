package com.desuzed.clocknweather

import android.app.Application
import com.desuzed.clocknweather.mvvm.repository.FavoriteLocationRepository
import com.desuzed.clocknweather.mvvm.repository.NetworkRepository
import com.desuzed.clocknweather.mvvm.room.RoomDbApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class App : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { RoomDbApp.getDatabase(this, applicationScope) }
    val favoriteLocationRepository by lazy { FavoriteLocationRepository(database.favoriteLocationDAO()) }
    val networkRepository by lazy { NetworkRepository(this) }

    override fun onCreate() {
        super.onCreate()
        instance = this

    }
    companion object {
        lateinit var instance: App
            private set
    }


}