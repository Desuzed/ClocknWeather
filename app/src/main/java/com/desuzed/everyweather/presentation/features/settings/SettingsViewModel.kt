package com.desuzed.everyweather.presentation.features.settings

import androidx.lifecycle.viewModelScope
import com.desuzed.everyweather.data.repository.local.SettingsRepository
import com.desuzed.everyweather.domain.model.settings.*
import com.desuzed.everyweather.presentation.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
) :
    BaseViewModel<SettingsState, SettingsAction>(SettingsState()) {

    init {
        collect(settingsRepository.darkMode, ::collectDarkTheme)
        collect(settingsRepository.lang, ::collectLanguage)
        collect(settingsRepository.distanceDimen, ::collectWindSpeed)
        collect(settingsRepository.tempDimen, ::collectTemperature)
    }

    fun onUserInteraction(interaction: SettingsUserInteraction) {
        when (interaction) {
            is SettingsUserInteraction.ChangeLanguage -> onLanguage(interaction.lang)
            is SettingsUserInteraction.ChangeDarkMode -> onDarkMode(interaction.darkMode)
            is SettingsUserInteraction.ChangeDistanceDimension -> onDistanceDimen(interaction.distanceDimen)
            is SettingsUserInteraction.ChangeTemperatureDimension -> onTemperatureDimen(interaction.tempDimen)
            is SettingsUserInteraction.ShowSettingDialog -> showSettingsDialog(interaction.type)
            SettingsUserInteraction.DismissDialog -> hideDialog()
        }
    }

    private fun showSettingsDialog(type: SettingsType) {
        setState { copy(showDialogType = type) }
    }

    private fun hideDialog() {
        setState { copy(showDialogType = null) }
    }

    private fun onDarkMode(darkMode: DarkMode) {
        viewModelScope.launch {
            settingsRepository.setDarkMode(darkMode)
            delay(500)
            hideDialog()
        }
    }

    private fun onLanguage(lang: Lang) {
        viewModelScope.launch {
            settingsRepository.setLanguage(lang)
            delay(500)
            hideDialog()
        }
    }

    private fun onDistanceDimen(distanceDimen: DistanceDimen) {
        viewModelScope.launch {
            settingsRepository.setDistanceDimension(distanceDimen)
            delay(500)
            hideDialog()
        }
    }

    private fun onTemperatureDimen(tempDimen: TempDimen) {
        viewModelScope.launch {
            settingsRepository.setTemperatureDimension(tempDimen)
            delay(500)
            hideDialog()
        }
    }

    private fun collectDarkTheme(darkTheme: DarkTheme) = setState {
        copy(darkTheme = darkTheme)
    }

    private fun collectLanguage(language: Language) = setState {
        copy(lang = language)
    }

    private fun collectWindSpeed(windSpeed: WindSpeed) = setState {
        copy(windSpeed = windSpeed)
    }

    private fun collectTemperature(temperature: Temperature) = setState {
        copy(tempDimen = temperature)
    }
}