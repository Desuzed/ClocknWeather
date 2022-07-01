package com.desuzed.everyweather.view.fragments.weather.next_days

import androidx.lifecycle.viewModelScope
import com.desuzed.everyweather.data.repository.RepositoryApp
import com.desuzed.everyweather.model.entity.WeatherResponse
import com.desuzed.everyweather.view.BaseViewModel
import com.desuzed.everyweather.view.ui.next_days.NextDaysUi
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