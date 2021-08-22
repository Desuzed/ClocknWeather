package com.desuzed.clocknweather.mvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.desuzed.clocknweather.retrofit.WeatherService
import com.desuzed.clocknweather.retrofit.OnecallApi
import com.desuzed.clocknweather.retrofit.RetrofitHandler
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherViewModel(app: Application) : AndroidViewModel(app) {
    private val appId = "9154a207f4437c78b9bbfbffeeb98e1a"
    private val baseUrl = "https://api.openweathermap.org/"
    val weatherLiveData = MutableLiveData<OnecallApi>()



    fun getOnecallForecast(lat: Double, lon: Double) {
        val retrofitHandler = RetrofitHandler ()
        retrofitHandler.getOnecallForecast(weatherLiveData, lat, lon)
    }

}