package com.desuzed.everyweather.di

import com.desuzed.everyweather.presentation.features.location_main.LocationUseCase
import com.desuzed.everyweather.presentation.features.weather_main.WeatherMainUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val useCaseModule = module {
    single {
        WeatherMainUseCase(
            sharedPrefsProvider = get(),
            remoteDataSource = get(),
            actionResultProvider = get(named(weatherResultProviderName)),
            weatherResponseMapper = get(),
            apiErrorMapper = get(),
        )
    }
    single {
        LocationUseCase(
            remoteDataSource = get(),
            actionResultProvider = get(named(geoResultProviderName)),
        )
    }
}