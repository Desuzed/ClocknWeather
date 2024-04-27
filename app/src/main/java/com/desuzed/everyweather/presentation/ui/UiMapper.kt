package com.desuzed.everyweather.presentation.ui

import android.content.Context
import com.desuzed.everyweather.domain.model.settings.DistanceDimen
import com.desuzed.everyweather.domain.model.settings.Lang
import com.desuzed.everyweather.domain.model.settings.PressureDimen
import com.desuzed.everyweather.domain.model.settings.TempDimen
import com.desuzed.everyweather.domain.model.weather.WeatherContent
import com.desuzed.everyweather.presentation.ui.main.MainWeatherMapper
import com.desuzed.everyweather.presentation.ui.main.WeatherMainUi
import com.desuzed.everyweather.presentation.ui.next_days.NextDaysMapper
import com.desuzed.everyweather.presentation.ui.next_days.NextDaysUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UiMapper(
    private val selectedDistanceDimen: DistanceDimen,
    private val selectedTemperature: TempDimen,
    private val selectedLanguage: Lang,
    private val selectedPressure: PressureDimen,
    context: Context,
) {
    private val resources = context.resources

    suspend fun mapToNextDaysUi(response: WeatherContent): List<NextDaysUi> =
        withContext(Dispatchers.Default) {
            NextDaysMapper(
                language = selectedLanguage,
                windSpeed = selectedDistanceDimen,
                temperature = selectedTemperature,
                resources = resources,
                pressure = selectedPressure,
            ).mapToNextDaysList(response)
        }

    suspend fun mapToMainWeatherUi(response: WeatherContent): WeatherMainUi =
        withContext(Dispatchers.Default) {
            MainWeatherMapper(
                resources = resources,
                windSpeed = selectedDistanceDimen,
                temperature = selectedTemperature,
                pressure = selectedPressure,
            ).mapToMainWeatherUi(response)
        }
}