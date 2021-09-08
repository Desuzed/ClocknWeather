package com.desuzed.clocknweather.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
//data/2.5/forecast?q={city name}&appid={API key}
    @GET("data/2.5/weather?&lang=ru&units=metric&APPID=$appId")
    suspend  fun getFiveDayForecast(@Query("q") city: String): Response<FiveDayForecast>

    @GET("data/2.5/onecall?&lang=ru&units=metric&APPID=$appId")
    suspend fun getForecastOnecall(
        @Query("lat") lat: String,
        @Query("lon") lon: String
    ): Response<OnecallApi>

    companion object{
        private val baseUrl = "https://api.openweathermap.org/"
        private const val appId = "9154a207f4437c78b9bbfbffeeb98e1a"
        var weatherService : WeatherService? = null
        fun getInstance() : WeatherService {
            if (weatherService == null){
                val interceptor = HttpLoggingInterceptor()
                interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
                val client = OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build()
                val retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
                weatherService = retrofit.create(WeatherService::class.java)
            }
            return weatherService!!
        }
    }
}