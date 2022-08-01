package com.desuzed.everyweather.data.network.retrofit

import com.desuzed.everyweather.BuildConfig
import com.desuzed.everyweather.data.network.dto.weatherApi.ErrorDtoWeatherApi
import com.desuzed.everyweather.data.network.dto.weatherApi.WeatherResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("forecast.json?key=${BuildConfig.WEATHER_API_KEY}&days=7")
    suspend fun getForecast(
        @Query("q") query: String,
        @Query("lang") lang: String
    ): NetworkResponse<WeatherResponseDto, ErrorDtoWeatherApi>
}