package com.desuzed.everyweather.presentation.features.weather_next_days

import androidx.lifecycle.viewModelScope
import com.desuzed.everyweather.domain.interactor.WeatherSettingsInteractor
import com.desuzed.everyweather.domain.model.settings.Language
import com.desuzed.everyweather.domain.model.settings.Pressure
import com.desuzed.everyweather.domain.model.settings.Temperature
import com.desuzed.everyweather.domain.model.settings.WindSpeed
import com.desuzed.everyweather.domain.repository.local.SharedPrefsProvider
import com.desuzed.everyweather.domain.repository.settings.SystemSettingsRepository
import com.desuzed.everyweather.presentation.base.BaseViewModel
import com.desuzed.everyweather.presentation.base.UserInteraction
import kotlinx.coroutines.launch

class NextDaysViewModel(
    private val sharedPrefsProvider: SharedPrefsProvider,
    weatherSettingsInteractor: WeatherSettingsInteractor,
    systemSettingsRepository: SystemSettingsRepository,
) : BaseViewModel<NextDaysState, NextDaysAction, UserInteraction>(NextDaysState()) {
    init {
        collect(weatherSettingsInteractor.distanceDimen, ::collectWindSpeed)
        collect(weatherSettingsInteractor.tempDimen, ::collectTemperature)
        collect(systemSettingsRepository.lang, ::collectLanguage)
        collect(weatherSettingsInteractor.pressureDimen, ::collectPressure)
        getCachedForecast()
    }

    private fun getCachedForecast() {
        viewModelScope.launch {
            val cachedForecast = sharedPrefsProvider.loadForecastFromCache()
            setState { copy(weather = cachedForecast) }
        }
    }

    private fun collectWindSpeed(windSpeed: WindSpeed) = setState { copy(windSpeed = windSpeed) }

    private fun collectLanguage(language: Language) = setState { copy(language = language) }

    private fun collectTemperature(temperature: Temperature) = setState {
        copy(temperature = temperature)
    }

    private fun collectPressure(pressure: Pressure) = setState {
        copy(pressure = pressure)
    }
}