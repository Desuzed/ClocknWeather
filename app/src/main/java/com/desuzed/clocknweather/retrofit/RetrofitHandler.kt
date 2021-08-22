package com.desuzed.clocknweather.retrofit

import androidx.lifecycle.MutableLiveData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHandler {
    private val appId = "9154a207f4437c78b9bbfbffeeb98e1a"
    private val baseUrl = "https://api.openweathermap.org/"
    var wService: WeatherService
    init {
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
        wService= retrofit.create(WeatherService::class.java)
    }

    fun getOnecallForecast (weatherLiveData: MutableLiveData<OnecallApi>, lat: Double, lon: Double){
        val call = wService.getForecastOnecall(lat.toString(), lon.toString(), appId)
        call.enqueue(object : Callback<OnecallApi?> {
            override fun onResponse(
                call: Call<OnecallApi?>,
                response: Response<OnecallApi?>
            ) {
                if (response.code() == 200) {
                    val onecallResponse = response.body()
                    weatherLiveData.value = onecallResponse
                }
            }
            override fun onFailure(call: Call<OnecallApi?>, t: Throwable) {
                //TODO Обработать ошибки
                val i = 0
            }
        })
    }
}