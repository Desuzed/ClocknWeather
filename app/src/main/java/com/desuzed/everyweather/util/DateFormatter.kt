package com.desuzed.everyweather.util

import java.text.SimpleDateFormat
import java.util.*

object DateFormatter {
    const val apiTimePattern = "hh:mm a"
    const val timePattern = "HH:mm"
    const val datePattern = "dd/MM"
    const val fullDatePattern = "E. dd/MM"
    const val hourPattern = "H"

    fun parse(pattern: String, value: String): Date? =
        SimpleDateFormat(pattern, Locale.getDefault()).parse(value)

    fun format(pattern: String, date: Date?): String =
        SimpleDateFormat(pattern, Locale.getDefault()).format(date ?: "")

    fun format(pattern: String, timeInMills: Long, timeZone: String): String {
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone(timeZone)
        return formatter.format(timeInMills.times(1000))
    }

    fun format(pattern: String, timeInMills: Long, timeZone: String, lang: String): String {
        val formatter = SimpleDateFormat(pattern, Locale(lang))
        formatter.timeZone = TimeZone.getTimeZone(timeZone)
        return formatter.format(timeInMills.times(1000))
    }

}