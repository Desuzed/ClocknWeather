package com.desuzed.everyweather.util.editor

import android.annotation.SuppressLint
import android.content.Context
import com.desuzed.everyweather.R
import com.desuzed.everyweather.model.model.ForecastDay
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class NextDaysEditor(private val forecastDay: ForecastDay, timeZone: String, context: Context) :
    StringEditor {
    @SuppressLint("SimpleDateFormat")
    private val sdfEddMM = SimpleDateFormat("E. dd/MM", Locale.getDefault())
    private val res = context.resources

    init {
        sdfEddMM.timeZone = TimeZone.getTimeZone(timeZone)
    }

    override fun getResultMap(): Map<String, String> {
        val resultMap = mutableMapOf<String, String>()
        val day = forecastDay.day
        val astro = forecastDay.astro
        resultMap["nextIcon"] = "https:${day.icon}"
        resultMap["tvNextDate"] = sdfEddMM.format(forecastDay.dateEpoch * 1000)
        resultMap["tvNextDescription"] = day.text
        resultMap["tvNextMaxTemp"] = day.maxTemp.roundToInt().toString() + res.getString(
            R.string.celsius
        )
        resultMap["tvNextMinTemp"] = day.minTemp.roundToInt().toString() + res.getString(
            R.string.celsius
        )
        resultMap["tvNextWind"] = "${day.maxWind} " + res.getString(R.string.kmh)
        resultMap["tvNextPressure"] =
            "${forecastDay.hourForecast[0].pressureMb} " + res.getString(R.string.mb)
        resultMap["tvNextHumidity"] = "${day.avgHumidity}%"
        resultMap["tvNextPop"] = "${day.popRain}%, ${day.totalPrecip} " + res.getString(R.string.mm)
        resultMap["tvNextSun"] = "${astro.sunrise}\n${astro.sunset}"
        resultMap["tvNextMoon"] = "${astro.moonrise}\n${astro.moonset}"
        return resultMap
    }
}