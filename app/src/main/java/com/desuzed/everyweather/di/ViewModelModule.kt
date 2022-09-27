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
            settingsDataStore = get(),
            sharedPrefsProvider = get(),
            userLocationProvider = get(),
        )
    }
    viewModel {
        LocationViewModel(
            roomProvider = get(),
            actionResultProvider = get(named(geoResultProviderName)),
            locationRepository = get(),
            settingsDataStore = get(),
            userLocationProvider = get(),
        )
    }
    viewModel {
        WeatherMainViewModel(
            weatherRepository = get(),
            sharedPrefsProvider = get(),
            actionResultProvider = get(named(weatherResultProviderName)),
            roomProvider = get(),
            settingsDataStore = get(),
        )
    }
    viewModel {
        NextDaysViewModel(
            sharedPrefsProvider = get(),
            settingsDataStore = get(),
        )
    }

    viewModel {
        SettingsViewModel(settingsDataStore = get())
    }

}