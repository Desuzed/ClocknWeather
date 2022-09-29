package com.desuzed.everyweather.di

import com.desuzed.everyweather.data.repository.providers.UserLocationProvider
import com.desuzed.everyweather.presentation.features.location_main.LocationRepository
import com.desuzed.everyweather.presentation.features.weather_main.WeatherRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val repositoryModule = module {
    single {
        WeatherRepository(
            sharedPrefsProvider = get(),
            remoteDataSource = get(),
            weatherResponseMapper = get(),
            apiErrorMapper = get(),
        )
    }
    single {
        LocationRepository(
            remoteDataSource = get(),
        )
    }

    single {
        UserLocationProvider(androidApplication())
    }

}