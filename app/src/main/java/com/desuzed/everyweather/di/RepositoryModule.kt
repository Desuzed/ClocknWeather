package com.desuzed.everyweather.di

import com.desuzed.everyweather.data.repository.providers.UserLocationProvider
import com.desuzed.everyweather.data.repository.providers.action_result.ActionResultProvider
import com.desuzed.everyweather.data.repository.providers.action_result.GeoActionResultProvider
import com.desuzed.everyweather.data.repository.providers.action_result.WeatherActionResultProvider
import com.desuzed.everyweather.presentation.features.location_main.LocationRepository
import com.desuzed.everyweather.presentation.features.weather_main.WeatherRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val weatherResultProviderName = "WeatherActionResultProvider"
const val geoResultProviderName = "GeoActionResultProvider"
val repositoryModule = module {
    single {
        WeatherRepository(
            sharedPrefsProvider = get(),
            remoteDataSource = get(),
            actionResultProvider = get(named(weatherResultProviderName)),
            weatherResponseMapper = get(),
            apiErrorMapper = get(),
        )
    }
    single {
        LocationRepository(
            remoteDataSource = get(),
            actionResultProvider = get(named(geoResultProviderName)),
        )
    }

    single {
        UserLocationProvider(androidApplication())
    }

    single<ActionResultProvider>(named(weatherResultProviderName)) {
        WeatherActionResultProvider(androidApplication().resources)
    }

    single<ActionResultProvider>(named(geoResultProviderName)) {
        GeoActionResultProvider(androidApplication().resources)
    }
}