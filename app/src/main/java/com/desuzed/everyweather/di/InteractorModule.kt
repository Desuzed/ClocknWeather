package com.desuzed.everyweather.di

import com.desuzed.everyweather.domain.interactor.LocationInteractor
import com.desuzed.everyweather.domain.interactor.SystemInteractor
import com.desuzed.everyweather.domain.interactor.SystemSettingsInteractor
import com.desuzed.everyweather.domain.interactor.WeatherInteractor
import com.desuzed.everyweather.domain.interactor.WeatherSettingsInteractor
import org.koin.dsl.module

val interactorModule = module {
    single {
        WeatherInteractor(
            remoteDataRepository = get(),
            systemSettingsRepository = get(),
            weatherDataRepository = get(),
        )
    }
    single {
        LocationInteractor(
            remoteDataRepository = get(),
            locationDb = get(),
            latLngKeyGenerator = get(),
            systemSettingsRepository = get(),
        )
    }

    single {
        WeatherSettingsInteractor(
            weatherSettingsRepository = get(),
        )
    }

    single {
        SystemInteractor(
            networkConnection = get(),
            sharedPrefsProvider = get(),
            userLocationProvider = get(),
        )
    }

    single {
        SystemSettingsInteractor(
            systemSettingsRepository = get(),
        )
    }
}