package com.desuzed.clocknweather.mvvm

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.desuzed.clocknweather.retrofit.FiveDayForecast
import com.desuzed.clocknweather.retrofit.OnecallApi
import com.desuzed.clocknweather.retrofit.WeatherService
import com.google.gson.Gson
import retrofit2.Response
import java.text.SimpleDateFormat

class Repository(val app: Application) {
    val checkBoxLiveData = MutableLiveData<CheckBoxStates>()

    init {
        val stateMin = app.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE)
            .getBoolean(STATE_MIN, false)
        val state15Min = app.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE)
            .getBoolean(STATE_15_MIN, false)
        val stateHour = app.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE)
            .getBoolean(STATE_HOUR, false)
        checkBoxLiveData.value = CheckBoxStates(stateMin, state15Min, stateHour)
    }


    fun saveForecast(onecall: OnecallApi, saveTime: Long) {
        val gson = Gson().toJson(onecall)
        app.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(ONECALL_KEY, gson)
            .putLong(SAVED_TIME_KEY, saveTime)
            .apply()
    }

    @SuppressLint("SimpleDateFormat")
    fun loadForecast(): Pair<String, OnecallApi> {
        val savedForecast = app.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE)
            .getString(ONECALL_KEY, NO_VALUE)
        val saveTime = app.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE)
            .getLong(SAVED_TIME_KEY, 0)
        if (savedForecast.equals(NO_VALUE)) {
            return Pair(NO_VALUE, OnecallApi())
        } else {
            val jsonForecast = Gson().fromJson(savedForecast, OnecallApi::class.java)
            return Pair("Время сохранения: ${SimpleDateFormat("d/MM/yy; k:mm:s").format(saveTime)}", jsonForecast)
        }
    }


    fun setState(checkBoxStates: CheckBoxStates) {
        checkBoxLiveData.value = checkBoxStates
        app.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(STATE_MIN, checkBoxStates.stateMinute)
            .putBoolean(STATE_15_MIN, checkBoxStates.state15min)
            .putBoolean(STATE_HOUR, checkBoxStates.stateHour)
            .apply()
    }

    suspend fun getForecastOnecall(lat: Double, lon: Double): Response<OnecallApi> {
        return WeatherService
            .getInstance()
            .getForecastOnecall(lat.toString(), lon.toString())
    }

    suspend fun getFiveDayForecast (city: String): Response<FiveDayForecast> {
        return WeatherService
            .getInstance()
            .getFiveDayForecast(city)
    }

    companion object {
        const val STATE_MIN = "STATE_MIN"
        const val STATE_15_MIN = "STATE_15_MIN"
        const val STATE_HOUR = "STATE_HOUR"
        const val S_PREF_NAME = "SP"
        const val ONECALL_KEY = "ONECALL"
        const val NO_VALUE = "NO_VALUE"
        const val SAVED_TIME_KEY = "SAVED_TIME_KEY"

    }
}