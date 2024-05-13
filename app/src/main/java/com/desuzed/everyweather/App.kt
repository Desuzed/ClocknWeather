package com.desuzed.everyweather

import android.app.Application
import com.desuzed.everyweather.di.analyticsModule
import com.desuzed.everyweather.di.interactorModule
import com.desuzed.everyweather.di.localDataSourceModule
import com.desuzed.everyweather.di.mapperModule
import com.desuzed.everyweather.di.networkModule
import com.desuzed.everyweather.di.repositoryModule
import com.desuzed.everyweather.di.utilModule
import com.desuzed.everyweather.di.viewModelModule
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
                interactorModule,
                utilModule,
                viewModelModule,
                analyticsModule,
            )
        }
    }

}