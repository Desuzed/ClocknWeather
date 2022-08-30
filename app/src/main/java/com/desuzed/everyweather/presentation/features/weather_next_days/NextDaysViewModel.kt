package com.desuzed.everyweather.presentation.features.weather_next_days

import androidx.lifecycle.viewModelScope
import com.desuzed.everyweather.data.repository.local.SettingsRepository
import com.desuzed.everyweather.domain.model.settings.Language
import com.desuzed.everyweather.domain.model.settings.Temperature
import com.desuzed.everyweather.domain.model.settings.WindSpeed
import com.desuzed.everyweather.domain.repository.local.SharedPrefsProvider
import com.desuzed.everyweather.presentation.base.BaseViewModel
import kotlinx.coroutines.launch

class NextDaysViewModel(
    private val sharedPrefsProvider: SharedPrefsProvider,
    settingsRepository: SettingsRepository,
) :
    BaseViewModel<NextDaysState, NextDaysAction>(NextDaysState()) {
    init {
        collect(settingsRepository.distanceDimen, ::collectWindSpeed)
        collect(settingsRepository.tempDimen, ::collectTemperature)
        collect(settingsRepository.lang, ::collectLanguage)

        viewModelScope.launch {
            val cachedForecast = sharedPrefsProvider.loadForecastFromCache()
            setState { copy(weather = cachedForecast) }
        }
    }

    private fun collectWindSpeed(windSpeed: WindSpeed) = setState { copy(windSpeed = windSpeed) }
    private fun collectLanguage(language: Language) = setState { copy(language = language) }
    private fun collectTemperature(temperature: Temperature) =
        setState { copy(temperature = temperature) }
}