package com.desuzed.everyweather.view.ui.main

import android.annotation.SuppressLint
import android.content.res.Resources
import com.desuzed.everyweather.R
import com.desuzed.everyweather.model.entity.WeatherResponse
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class WeatherMainInfoUi(response: WeatherResponse, res: Resources) {
    @SuppressLint("SimpleDateFormat")
    private val sdfDate = SimpleDateFormat("dd/MM")

    @SuppressLint("SimpleDateFormat")
    private val sdfTime = SimpleDateFormat("HH:mm")
    private val localTime = response.location.localtimeEpoch.times(1000)

    val timeZone = response.location.timezone
    val location = response.location
    val currentTemp: String
    val geoText: String
    val feelsLike: String
    val date: String
    val time: String
    val description: String
    val iconUrl: String

    init {
        sdfDate.timeZone = TimeZone.getTimeZone(timeZone)
        sdfTime.timeZone = TimeZone.getTimeZone(timeZone)
        val current = response.current
        iconUrl = "https:${current.icon}"
        date = sdfDate.format(localTime)
        time = sdfTime.format(localTime)
        geoText = response.location.toString()
        currentTemp = current.temp.roundToInt().toString() + res.getString(R.string.celsius)
        description = current.text
        feelsLike = res.getString(R.string.feels_like) + " ${current.feelsLike.roundToInt()}" +
                res.getString(R.string.celsius)
    }
}



