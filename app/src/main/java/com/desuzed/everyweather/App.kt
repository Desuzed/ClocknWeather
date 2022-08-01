package com.desuzed.everyweather

import android.app.Application
import com.desuzed.everyweather.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                localDataSourceModule,
                networkModule,
                mapperModule,
                useCaseModule,
                utilModule,
                viewModelModule,
            )
        }
    }

}