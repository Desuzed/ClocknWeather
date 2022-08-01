package com.desuzed.everyweather.di

import com.desuzed.everyweather.util.ActionResultProvider
import com.desuzed.everyweather.util.ActionResultProviderImpl
import com.desuzed.everyweather.util.NetworkConnection
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val utilModule = module {
    single<ActionResultProvider> {
        ActionResultProviderImpl(androidApplication().resources)
    }
    single {
        NetworkConnection(androidApplication())
    }
}