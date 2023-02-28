package com.desuzed.everyweather.di

import com.desuzed.everyweather.analytics.LocationMainAnalytics
import com.desuzed.everyweather.analytics.MapLocationAnalytics
import com.desuzed.everyweather.analytics.SettingsAnalytics
import com.desuzed.everyweather.analytics.WeatherMainAnalytics
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
}