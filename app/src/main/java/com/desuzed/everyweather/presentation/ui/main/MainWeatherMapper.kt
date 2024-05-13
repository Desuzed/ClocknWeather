package com.desuzed.everyweather.presentation.ui.main

import android.content.res.Resources
import com.desuzed.everyweather.domain.model.settings.DistanceDimen
import com.desuzed.everyweather.domain.model.settings.PressureDimen
import com.desuzed.everyweather.domain.model.settings.TempDimen
import com.desuzed.everyweather.domain.model.weather.WeatherContent
import com.desuzed.everyweather.presentation.ui.HourUi

class MainWeatherMapper(
    private val windSpeed: DistanceDimen,
    private val temperature: TempDimen,
    private val pressure: PressureDimen,
    private val resources: Resources
) {

    fun mapToMainWeatherUi(response: WeatherContent): WeatherMainUi {
        val weatherMainInfo = WeatherMainInfoUi(
            temperature = temperature,
            response = response,
            res = resources
        )
        val hourList = HourUi.generateCurrentDayList(response).map {
            HourUi(
                windSpeed = windSpeed,
                temperature = temperature,
                hour = it,
                timeZone = response.location.timezone,
                res = resources
            )
        }
        val detailCard = DetailCardMain(
            response = response,
            res = resources,
            windSpeed = windSpeed,
            pressureDimen = pressure,
        )
        return WeatherMainUi(
            mainInfo = weatherMainInfo,
            hourList = hourList,
            detailCard = detailCard
        )
    }
}