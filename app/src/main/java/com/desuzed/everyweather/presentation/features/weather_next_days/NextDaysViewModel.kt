package com.desuzed.everyweather.presentation.features.weather_next_days

import androidx.lifecycle.viewModelScope
import com.desuzed.everyweather.domain.interactor.WeatherSettingsInteractor
import com.desuzed.everyweather.domain.model.settings.DistanceDimen
import com.desuzed.everyweather.domain.model.settings.Lang
import com.desuzed.everyweather.domain.model.settings.PressureDimen
import com.desuzed.everyweather.domain.model.settings.TempDimen
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

    private fun collectWindSpeed(windSpeed: DistanceDimen) =
        setState { copy(windSpeed = windSpeed) }

    private fun collectLanguage(language: Lang) = setState { copy(selectedLang = language) }

    private fun collectTemperature(temperature: TempDimen) = setState {
        copy(temperature = temperature)
    }

    private fun collectPressure(pressure: PressureDimen) = setState {
        copy(pressure = pressure)
    }
}