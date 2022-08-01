package com.desuzed.everyweather.di

import com.desuzed.everyweather.presentation.features.locatation_map.MapLocationViewModel
import com.desuzed.everyweather.presentation.features.location_main.LocationViewModel
import com.desuzed.everyweather.presentation.features.main_activity.MainActivityViewModel
import com.desuzed.everyweather.presentation.features.weather_main.WeatherMainViewModel
import com.desuzed.everyweather.presentation.features.weather_next_days.NextDaysViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MapLocationViewModel(sharedPrefsProvider = get())
    }
    viewModel {
        MainActivityViewModel(networkConnection = get())
    }
    viewModel {
        LocationViewModel(roomProvider = get(), actionResultProvider = get())
    }
    viewModel {
        WeatherMainViewModel(
            useCase = get(),
            uiMapper = get(),
            sharedPrefsProvider = get(),
            actionResultProvider = get(),
            roomProvider = get(),
        )
    }
    viewModel {
        NextDaysViewModel(sharedPrefsProvider = get(), uiMapper = get())
    }
}