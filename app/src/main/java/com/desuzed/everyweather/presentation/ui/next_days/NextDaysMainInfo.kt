package com.desuzed.everyweather.presentation.ui.next_days

import android.content.res.Resources
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.ForecastDay
import com.desuzed.everyweather.util.DateFormatter
import kotlin.math.roundToInt

class NextDaysMainInfo(
    forecastDay: ForecastDay,
    timeZone: String,
    res: Resources,
) {
    val iconUrl: String
    val date: String
    val description: String
    val maxTemp: String
    val minTemp: String

    init {
        val day = forecastDay.day
        iconUrl = "https:${day.icon}"
        date = DateFormatter.format(
            pattern = DateFormatter.fullDatePattern,
            timeInMills = forecastDay.dateEpoch,
            timeZone = timeZone
        )
        description = day.text
        maxTemp = day.maxTemp.roundToInt().toString() + res.getString(
            R.string.celsius
        )
        minTemp = day.minTemp.roundToInt().toString() + res.getString(
            R.string.celsius
        )
    }
}