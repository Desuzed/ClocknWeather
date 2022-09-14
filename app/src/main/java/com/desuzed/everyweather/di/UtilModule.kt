package com.desuzed.everyweather.di

import com.desuzed.everyweather.util.NetworkConnection
import com.desuzed.everyweather.util.action_result.ActionResultProvider
import com.desuzed.everyweather.util.action_result.GeoActionResultProvider
import com.desuzed.everyweather.util.action_result.WeatherActionResultProvider
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val weatherResultProviderName = "WeatherActionResultProvider"
const val geoResultProviderName = "GeoActionResultProvider"
val utilModule = module {
    single<ActionResultProvider>(named(weatherResultProviderName)) {
        WeatherActionResultProvider(androidApplication().resources)
    }
    single<ActionResultProvider>(named(geoResultProviderName)) {
        GeoActionResultProvider(androidApplication().resources)
    }
    single {
        NetworkConnection(androidApplication())
    }
}