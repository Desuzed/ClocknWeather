package com.desuzed.everyweather

import android.app.Application
import com.desuzed.everyweather.mvvm.repository.LocalDataSourceImpl
import com.desuzed.everyweather.mvvm.repository.RemoteDataSourceImpl
import com.desuzed.everyweather.mvvm.repository.RepositoryApp
import com.desuzed.everyweather.mvvm.room.RoomDbApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class App : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { RoomDbApp.getDatabase(this, applicationScope) }
    private val localDataSource by lazy { LocalDataSourceImpl(database.favoriteLocationDAO(), this) }
    private val remoteDataSource by lazy { RemoteDataSourceImpl() }
    val repositoryApp by lazy { RepositoryApp(localDataSource, remoteDataSource) }

    fun setLang (lang : String){
        remoteDataSource.lang = lang
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

    }
    companion object {
        lateinit var instance: App
            private set
    }


}