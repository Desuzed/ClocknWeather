package com.desuzed.everyweather.di

import com.desuzed.everyweather.domain.util.LatLngKeyGenerator
import com.desuzed.everyweather.util.LatLngKeyGeneratorImpl
import com.desuzed.everyweather.util.NetworkConnection
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val utilModule = module {
    single {
        NetworkConnection(androidApplication())
    }
    single<LatLngKeyGenerator> {
        LatLngKeyGeneratorImpl()
    }
}