package com.desuzed.everyweather.di

import androidx.room.Room
import com.desuzed.everyweather.data.repository.local.RoomProviderImpl
import com.desuzed.everyweather.data.repository.local.SettingsDataStore
import com.desuzed.everyweather.data.repository.local.SharedPrefsProviderImpl
import com.desuzed.everyweather.data.repository.remote.RemoteDataSourceImpl
import com.desuzed.everyweather.data.room.RoomDbApp
import com.desuzed.everyweather.domain.repository.local.RoomProvider
import com.desuzed.everyweather.domain.repository.local.SharedPrefsProvider
import com.desuzed.everyweather.domain.repository.remote.RemoteDataSource
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val localDataSourceModule = module {
    single {
        Room.databaseBuilder(androidApplication(), RoomDbApp::class.java, RoomDbApp.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    single {
        get<RoomDbApp>().favoriteLocationDAO()
    }

    single<RoomProvider> {
        RoomProviderImpl(
            favoriteLocationDAO = get()
        )
    }

    single<SharedPrefsProvider> {
        SharedPrefsProviderImpl(androidApplication())
    }

    single<RemoteDataSource> {
        RemoteDataSourceImpl(weatherApi = get(), locationIqApi = get())
    }

    single<SettingsDataStore> {
        SettingsDataStore(context = androidApplication())
    }

}