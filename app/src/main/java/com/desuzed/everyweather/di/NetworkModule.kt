package com.desuzed.everyweather.di

import android.util.Log
import com.desuzed.everyweather.Config
import com.desuzed.everyweather.data.network.retrofit.NetworkResponseAdapterFactory
import com.desuzed.everyweather.data.network.retrofit.WeatherApiService
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
    single {
        val interceptor = HttpLoggingInterceptor {
            Log.i("INTERCEPTOR", it)
        }
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
        val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
        if (Config.debug) {
            clientBuilder.addInterceptor(interceptor)
        }
        clientBuilder.build()
    }
    single(named("api")) {
        Retrofit.Builder()
            .baseUrl(Config.baseUrl)
            .addCallAdapterFactory(get())
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }
    single { get<Retrofit>(named("api")).create(WeatherApiService::class.java) }

}