package com.desuzed.everyweather.view.entity

import android.annotation.SuppressLint
import android.content.res.Resources
import com.desuzed.everyweather.R
import com.desuzed.everyweather.model.entity.Hour
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class HourEntityView(
    hour: Hour,
    timeZone: String,
    res: Resources
) {
    @SuppressLint("SimpleDateFormat")
    private val sdfHHmm = SimpleDateFormat("HH:mm")
    val time: String
    val temp: String
    val wind: String
    val iconUrl: String
    val rotation: Float

    init {
        sdfHHmm.timeZone = TimeZone.getTimeZone(timeZone)
        time = sdfHHmm.format(hour.timeEpoch * 1000)
        temp = hour.temp.roundToInt().toString() + res.getString(R.string.celsius)
        wind = "${hour.windSpeed.toInt()} " + res.getString(R.string.kmh)
        iconUrl = "https:${hour.icon}"
        rotation = hour.windDegree.toFloat() - 180
    }
}