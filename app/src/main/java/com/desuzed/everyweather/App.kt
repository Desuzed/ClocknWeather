package com.desuzed.everyweather

import android.app.Application
import com.desuzed.everyweather.data.repository.*
import com.desuzed.everyweather.data.room.RoomDbApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class App : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { RoomDbApp.getDatabase(this, applicationScope) }
    private val roomProvider by lazy { RoomProviderImpl (database.favoriteLocationDAO()) }
    private val sPrefProvider by lazy { SPrefProviderImpl(this)}
    private val localDataSource by lazy { LocalDataSourceImpl(roomProvider, this, sPrefProvider) }
    private val remoteDataSource by lazy { RemoteDataSourceImpl() }
    private val repositoryApp by lazy { RepositoryAppImpl(localDataSource, remoteDataSource) }


    fun getRepo () : RepositoryApp = repositoryApp

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