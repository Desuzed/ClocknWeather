package com.desuzed.everyweather.di

import com.desuzed.everyweather.analytics.*
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val analyticsModule = module {
    single {
        SettingsAnalytics(androidApplication())
    }
    single {
        MapLocationAnalytics(androidApplication())
    }
    single {
        WeatherMainAnalytics(androidApplication())
    }
    single {
        LocationMainAnalytics(androidApplication())
    }
    single {
        InAppUpdateAnalytics(androidApplication())
    }
}