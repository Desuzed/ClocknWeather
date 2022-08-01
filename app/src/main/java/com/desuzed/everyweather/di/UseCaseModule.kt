package com.desuzed.everyweather.di

import com.desuzed.everyweather.presentation.features.weather_main.WeatherMainUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single {
        WeatherMainUseCase(
            sharedPrefsProvider = get(),
            remoteDataSource = get(),
            actionResultProvider = get(),
            weatherResponseMapper = get(),
            apiErrorMapper = get(),
        )
    }
}