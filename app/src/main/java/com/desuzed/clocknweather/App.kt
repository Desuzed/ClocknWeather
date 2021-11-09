package com.desuzed.clocknweather

import android.app.Application
import com.desuzed.clocknweather.mvvm.repository.LocalDataSourceImpl
import com.desuzed.clocknweather.mvvm.repository.RemoteDataSourceImpl
import com.desuzed.clocknweather.mvvm.repository.RepositoryApp
import com.desuzed.clocknweather.mvvm.room.RoomDbApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class App : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { RoomDbApp.getDatabase(this, applicationScope) }
    private val localDataSource by lazy { LocalDataSourceImpl(database.favoriteLocationDAO(), this) }
    private val remoteDataSource by lazy { RemoteDataSourceImpl() }
    val repositoryApp by lazy { RepositoryApp(localDataSource, remoteDataSource) }

    override fun onCreate() {
        super.onCreate()
        instance = this

    }
    companion object {
        lateinit var instance: App
            private set
    }


}