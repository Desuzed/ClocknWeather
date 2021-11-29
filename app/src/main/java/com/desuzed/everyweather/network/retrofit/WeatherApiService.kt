package com.desuzed.everyweather.network.retrofit

import com.desuzed.everyweather.BuildConfig
import com.desuzed.everyweather.network.dto.ApiErrorDto
import com.desuzed.everyweather.network.dto.LocationDto
import com.desuzed.everyweather.network.dto.WeatherResponseDto
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("forecast.json?key=${BuildConfig.WEATHER_API_KEY}&days=7")
    suspend fun getForecast(
        @Query("q") query: String,
        @Query("lang") lang: String
    ): NetworkResponse<WeatherResponseDto, ApiErrorDto>

    companion object{
        private const val baseUrl = "https://api.weatherapi.com/v1/"
        private var weatherApiService : WeatherApiService? = null
        fun getInstance() : WeatherApiService {
            if (weatherApiService == null){
                val interceptor = HttpLoggingInterceptor()
                interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
                val client = OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build()
                val retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addCallAdapterFactory(NetworkResponseAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
                weatherApiService = retrofit.create(WeatherApiService::class.java)
            }
            return weatherApiService!!
        }
    }
}