package com.desuzed.everyweather.presentation.features.weather_next_days

import androidx.lifecycle.viewModelScope
import com.desuzed.everyweather.data.repository.RepositoryApp
import com.desuzed.everyweather.domain.model.WeatherResponse
import com.desuzed.everyweather.presentation.base.BaseViewModel
import com.desuzed.everyweather.presentation.ui.next_days.NextDaysUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NextDaysViewModel(private val repo: RepositoryApp) :
    BaseViewModel<NextDaysState, NextDaysAction>(NextDaysState()) {
    init {
        viewModelScope.launch {
            val cachedForecast = repo.loadForecastFromCache()
            if (cachedForecast != null) {
                val list = mapToUi(cachedForecast)
                setState { copy(nextDaysUiList = list) }
            }
        }
    }

    private suspend fun mapToUi(weatherResponse: WeatherResponse): List<NextDaysUi> =
        withContext(Dispatchers.IO) {
            repo.mapToNextDaysUi(weatherResponse)
        }
}