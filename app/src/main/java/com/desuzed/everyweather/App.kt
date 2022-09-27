package com.desuzed.everyweather

import android.app.Application
import com.desuzed.everyweather.di.*
import com.google.firebase.crashlytics.FirebaseCrashlytics
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                localDataSourceModule,
                networkModule,
                mapperModule,
                repositoryModule,
                utilModule,
                viewModelModule,
                analyticsModule,
            )
        }
    }

}