package com.desuzed.clocknweather

import com.desuzed.clocknweather.mvvm.OnecallApi
import com.desuzed.clocknweather.mvvm.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("data/2.5/weather?")
    fun getWeatherCurrentLocation(
        @Query("lat") lat: String?,
        @Query("lon") lon: String?,
        @Query("APPID") app_id: String?,
        @Query("units") units: String?,
        @Query("lang") lang: String?
    ): Call<WeatherResponse?>?

    @GET("data/2.5/weather?")
    fun getCurrentWeatherDataByCity(
        @Query("q") city: String?,
        @Query("APPID") app_id: String?,
        @Query("units") units: String?,
        @Query("lang") lang: String?
    ): Call<WeatherResponse>

    @GET("data/2.5/onecall?&lang=ru&units=metric")
    fun getForecastOnecall(
        @Query("lat") lat: String?,
        @Query("lon") lon: String?,
        @Query("APPID") app_id: String?
    ): Call<OnecallApi>
}