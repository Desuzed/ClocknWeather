package com.desuzed.everyweather.di

import android.util.Log
import com.desuzed.everyweather.Config
import com.desuzed.everyweather.data.network.retrofit.NetworkResponseAdapterFactory
import com.desuzed.everyweather.data.network.retrofit.api.LocationIqApi
import com.desuzed.everyweather.data.network.retrofit.api.WeatherApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single<CallAdapter.Factory> { NetworkResponseAdapterFactory() }
    single<OkHttpClient> {
        val interceptor = HttpLoggingInterceptor {
            Log.i("INTERCEPTOR", it)
        }
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
        val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(Config.TIMEOUT, TimeUnit.SECONDS)
        if (Config.debug) {
            clientBuilder.addInterceptor(interceptor)
        }
        clientBuilder.build()
    }
    single(named(Config.weatherApiName)) {
        Retrofit.Builder()
            .baseUrl(Config.baseWeatherApiUrl)
            .addCallAdapterFactory(get())
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }
    single { get<Retrofit>(named(Config.weatherApiName)).create(WeatherApiService::class.java) }

    single(named(Config.locationIqApi)) {
        Retrofit.Builder()
            .baseUrl(Config.locationIqBaseUrl)
            .addCallAdapterFactory(get())
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }
    single { get<Retrofit>(named(Config.locationIqApi)).create(LocationIqApi::class.java) }
}