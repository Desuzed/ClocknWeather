package com.desuzed.everyweather.di

import com.desuzed.everyweather.presentation.features.locatation_map.MapLocationViewModel
import com.desuzed.everyweather.presentation.features.location_main.LocationViewModel
import com.desuzed.everyweather.presentation.features.main_activity.MainActivityViewModel
import com.desuzed.everyweather.presentation.features.settings.SettingsViewModel
import com.desuzed.everyweather.presentation.features.weather_main.WeatherMainViewModel
import com.desuzed.everyweather.presentation.features.weather_next_days.NextDaysViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MapLocationViewModel(
            sharedPrefsProvider = get(),
            analytics = get(),
        )
    }
    viewModel {
        MainActivityViewModel(
            networkConnection = get(),
            settingsDataStore = get(),
            sharedPrefsProvider = get(),
            userLocationProvider = get(),
        )
    }
    viewModel {
        LocationViewModel(
            roomProvider = get(),
            locationRepository = get(),
            settingsDataStore = get(),
            userLocationProvider = get(),
            analytics = get(),
        )
    }
    viewModel {
        WeatherMainViewModel(
            weatherRepository = get(),
            sharedPrefsProvider = get(),
            roomProvider = get(),
            settingsDataStore = get(),
            analytics = get(),
        )
    }
    viewModel {
        NextDaysViewModel(
            sharedPrefsProvider = get(),
            settingsDataStore = get(),
        )
    }

    viewModel {
        SettingsViewModel(
            settingsDataStore = get(),
            analytics = get(),
        )
    }

}