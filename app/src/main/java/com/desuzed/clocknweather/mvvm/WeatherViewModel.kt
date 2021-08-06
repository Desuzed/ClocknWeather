package com.desuzed.clocknweather.mvvm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.desuzed.clocknweather.WeatherService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class WeatherViewModel(app: Application) : AndroidViewModel(app) {
    private val appId = "9154a207f4437c78b9bbfbffeeb98e1a"
    private val baseUrl = "https://api.openweathermap.org/"
    val weatherLiveData = MutableLiveData<String>()

    fun setCurrentWeatherByCity(city: String) {
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
        val wService= retrofit.create(WeatherService::class.java)
        val call = wService.getCurrentWeatherDataByCity(city, appId, "metric", "ru")
        call.enqueue(object : Callback<WeatherResponse?> {
            override fun onResponse(
                call: Call<WeatherResponse?>,
                response: Response<WeatherResponse?>
            ) {
                var cityWeather: String = ""
                if (response.code() == 200) {
                    val weatherResponse = response.body()
                    if (weatherResponse != null) {
                        //        val temp = weatherResponse.main.temp - 273
                        cityWeather = "Страна: ${weatherResponse.sys.country}\n" +
                                "Температура: ${weatherResponse.main.temp}\n" +
                                "Влажность: ${weatherResponse.main.humidity}\n" +
                                "Давление: ${weatherResponse.main.pressure}\n" +
                                "Место: ${weatherResponse.name}\n" +
                                "Sunrise: ${SimpleDateFormat("hh:mm:ss").format(weatherResponse.sys.sunrise)}\n" +
                                "Sunset: ${SimpleDateFormat("hh:mm:ss").format(weatherResponse.sys.sunset)}\n"
                        // val date = SimpleDateFormat("hh:mm:ss").format(weatherResponse.sys.sunrise)
                    }
                }
                weatherLiveData.value = cityWeather
            }

            override fun onFailure(call: Call<WeatherResponse?>, t: Throwable) {
                //TODO Обработать ошибки
            }
        })
    }


    fun getForecastOnecall(lat: String, lon: String) {
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
        val wService= retrofit.create(WeatherService::class.java)
        val call = wService.getForecastOnecall(lat, lon, appId)
        call.enqueue(object : Callback<OnecallApi?> {
            override fun onResponse(
                call: Call<OnecallApi?>,
                response: Response<OnecallApi?>
            ) {
                var cityWeather: String = ""
                if (response.code() == 200) {
                    val onecallResponse = response.body()
                    if (onecallResponse != null) {
                        cityWeather = "Страна: ${onecallResponse.timezone}\n" +
                                "Date: ${SimpleDateFormat("hh:mm:ss").format(onecallResponse.current?.date?.minus(
                                    onecallResponse.timezone_offset
                                ))}\n" +
                                "Температура: ${onecallResponse.current?.temp}\n" +
                                "Влажность: ${onecallResponse.current?.humidity}\n" +
                                "Давление: ${onecallResponse.current?.pressure}\n" +
                                "Место: ${onecallResponse.timezone}\n" +
                                "Sunrise: ${SimpleDateFormat("hh:mm:ss").format(onecallResponse.current?.sunrise)}\n" +
                                "Sunset: ${SimpleDateFormat("hh:mm:ss").format(onecallResponse.current?.sunset)}\n"//TODO Неправильно парсит дату из милисекунд

                    }
                }

                weatherLiveData.value = cityWeather
            }

            override fun onFailure(call: Call<OnecallApi?>, t: Throwable) {
                //TODO Обработать ошибки
                val i = 0
            }
        })
    }

}