package com.desuzed.everyweather.di

import com.desuzed.everyweather.presentation.features.locatation_map.MapLocationViewModel
import com.desuzed.everyweather.presentation.features.location_main.LocationViewModel
import com.desuzed.everyweather.presentation.features.main_activity.MainActivityViewModel
import com.desuzed.everyweather.presentation.features.settings.SettingsViewModel
import com.desuzed.everyweather.presentation.features.weather_main.WeatherMainViewModel
import com.desuzed.everyweather.presentation.features.weather_next_days.NextDaysViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MapLocationViewModel(sharedPrefsProvider = get())
    }
    viewModel {
        MainActivityViewModel(
            networkConnection = get(),
            settingsRepository = get(),
            sharedPrefsProvider = get(),
        )
    }
    viewModel {
        LocationViewModel(
            roomProvider = get(),
            actionResultProvider = get(named(geoResultProviderName)),
            locationUseCase = get(),
            settingsRepository = get(),
        )
    }
    viewModel {
        WeatherMainViewModel(
            useCase = get(),
            sharedPrefsProvider = get(),
            actionResultProvider = get(named(weatherResultProviderName)),
            roomProvider = get(),
            settingsRepository = get(),
        )
    }
    viewModel {
        NextDaysViewModel(
            sharedPrefsProvider = get(),
            settingsRepository = get(),
        )
    }

    viewModel {
        SettingsViewModel(settingsRepository = get())
    }

}