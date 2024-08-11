package com.desuzed.everyweather.di

import androidx.room.Room
import com.desuzed.everyweather.data.repository.local.LocationDbImpl
import com.desuzed.everyweather.data.repository.local.SharedPrefsProviderImpl
import com.desuzed.everyweather.data.repository.local.WeatherDataStore
import com.desuzed.everyweather.data.room.RoomDbApp
import com.desuzed.everyweather.domain.repository.local.LocationDb
import com.desuzed.everyweather.domain.repository.local.SharedPrefsProvider
import com.desuzed.everyweather.domain.repository.settings.SettingsDatastoreApiProvider
import kotlinx.coroutines.Dispatchers
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

    single<LocationDb> {
        LocationDbImpl(
            favoriteLocationDAO = get(),
            favoriteLocationMapper = get(),
            latLngKeyGenerator = get(),
            dispatcher = Dispatchers.IO,
        )
    }

    single<SharedPrefsProvider> {
        SharedPrefsProviderImpl(androidApplication())
    }

    single {
        SettingsDatastoreApiProvider(context = androidApplication())
    }

    single {
        WeatherDataStore(context = androidApplication())
    }

}