package com.desuzed.everyweather.di

import com.desuzed.everyweather.presentation.features.in_app_update.InAppUpdateViewModel
import com.desuzed.everyweather.presentation.features.locatation_map.MapLocationViewModel
import com.desuzed.everyweather.presentation.features.location_main.LocationViewModel
import com.desuzed.everyweather.presentation.features.main_activity.MainActivityViewModel
import com.desuzed.everyweather.presentation.features.settings.SettingsViewModel
import com.desuzed.everyweather.presentation.features.shared.SharedViewModel
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
            systemSettingsInteractor = get(),
            systemInteractor = get(),
        )
    }
    viewModel {
        LocationViewModel(
            locationInteractor = get(),
            userLocationProvider = get(),
            analytics = get(),
        )
    }
    viewModel {
        WeatherMainViewModel(
            weatherInteractor = get(),
            weatherSettingsInteractor = get(),
            systemSettingsRepository = get(),
            locationInteractor = get(),
            analytics = get(),
        )
    }
    viewModel {
        NextDaysViewModel(
            sharedPrefsProvider = get(),
            weatherSettingsInteractor = get(),
            systemSettingsRepository = get(),
        )
    }

    viewModel {
        SettingsViewModel(
            weatherSerringsInteractor = get(),
            systemSettingsInteractor = get(),
            analytics = get(),
            appUpdateProvider = get(),
        )
    }

    viewModel {
        InAppUpdateViewModel(
            analytics = get(),
        )
    }

    viewModel {
        SharedViewModel(
            appUpdateProvider = get(),
        )
    }
}