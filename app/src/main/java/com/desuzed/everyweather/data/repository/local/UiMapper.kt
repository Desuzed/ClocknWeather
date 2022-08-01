package com.desuzed.everyweather.data.repository.local

import android.content.Context
import com.desuzed.everyweather.domain.model.WeatherResponse
import com.desuzed.everyweather.presentation.ui.main.MainWeatherMapper
import com.desuzed.everyweather.presentation.ui.main.WeatherMainUi
import com.desuzed.everyweather.presentation.ui.next_days.NextDaysMapper
import com.desuzed.everyweather.presentation.ui.next_days.NextDaysUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UiMapper(context: Context) {
    private val resources = context.resources

    suspend fun mapToNextDaysUi(response: WeatherResponse): List<NextDaysUi> =
        withContext(Dispatchers.IO) {
            NextDaysMapper(resources).mapToNextDaysList(response)
        }

    suspend fun mapToMainWeatherUi(response: WeatherResponse): WeatherMainUi =
        withContext(Dispatchers.IO) {
            MainWeatherMapper(resources).mapToMainWeatherUi(response)
        }
}