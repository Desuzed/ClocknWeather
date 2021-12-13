package com.desuzed.everyweather.util.editor

import android.annotation.SuppressLint
import android.content.Context
import com.desuzed.everyweather.R
import com.desuzed.everyweather.model.model.Hour
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class HourStringEditor(
    private val hour: Hour,
    timeZone: String,
    context: Context
) : StringEditor {
    @SuppressLint("SimpleDateFormat")
    private val sdfHHmm = SimpleDateFormat("HH:mm")
    private val res = context.resources

    init {
        sdfHHmm.timeZone = TimeZone.getTimeZone(timeZone)
    }

    override fun getResultMap(): Map<String, String> {
        val resultMap = mutableMapOf<String, String>()
        resultMap["hTime"] = sdfHHmm.format(hour.timeEpoch * 1000)
        resultMap["hTempC"] = hour.temp.roundToInt().toString() + res.getString(R.string.celsius)
        resultMap["hWind"] = "${hour.windSpeed.toInt()} " + res.getString(R.string.kmh)
        resultMap["hIcon"] = "https:${hour.icon}"
        return resultMap
    }
}