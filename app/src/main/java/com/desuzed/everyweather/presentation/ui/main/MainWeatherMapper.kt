package com.desuzed.everyweather.presentation.ui.main

import android.content.res.Resources
import com.desuzed.everyweather.domain.model.WeatherResponse
import com.desuzed.everyweather.presentation.ui.HourUi

class MainWeatherMapper(private val resources: Resources) {

    fun mapToMainWeatherUi(response: WeatherResponse): WeatherMainUi {
        val weatherMainInfo = WeatherMainInfoUi(response = response, res = resources)
        val hourList = HourUi.generateCurrentDayList(response).map {
            HourUi(
                hour = it,
                timeZone = response.location.timezone,
                res = resources
            )
        }
        val detailCard = DetailCardMain(
            response = response,
            res = resources,
        )
        return WeatherMainUi(
            mainInfo = weatherMainInfo,
            hourList = hourList,
            detailCard = detailCard
        )
    }
}