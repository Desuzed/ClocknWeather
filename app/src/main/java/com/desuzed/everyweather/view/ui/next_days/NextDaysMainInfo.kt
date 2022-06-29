package com.desuzed.everyweather.view.ui.next_days

import android.annotation.SuppressLint
import android.content.res.Resources
import com.desuzed.everyweather.R
import com.desuzed.everyweather.model.entity.ForecastDay
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class NextDaysMainInfo(
    forecastDay: ForecastDay,
    timeZone: String,
    res: Resources,
) {
    @SuppressLint("SimpleDateFormat")
    private val sdfEddMM = SimpleDateFormat("E. dd/MM", Locale.getDefault())
    val iconUrl: String
    val date: String
    val description: String
    val maxTemp: String
    val minTemp: String

    init {
        sdfEddMM.timeZone = TimeZone.getTimeZone(timeZone)
        val day = forecastDay.day
        iconUrl = "https:${day.icon}"
        date = sdfEddMM.format(forecastDay.dateEpoch * 1000)
        description = day.text
        maxTemp = day.maxTemp.roundToInt().toString() + res.getString(
            R.string.celsius
        )
        minTemp = day.minTemp.roundToInt().toString() + res.getString(
            R.string.celsius
        )
    }
}