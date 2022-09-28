package com.desuzed.everyweather.presentation.features.settings

import androidx.lifecycle.viewModelScope
import com.desuzed.everyweather.analytics.SettingsAnalytics
import com.desuzed.everyweather.data.repository.local.SettingsDataStore
import com.desuzed.everyweather.domain.model.settings.*
import com.desuzed.everyweather.presentation.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsDataStore: SettingsDataStore,
    private val analytics: SettingsAnalytics,
) : BaseViewModel<SettingsState, SettingsAction>(SettingsState()) {

    init {
        collect(settingsDataStore.darkMode, ::collectDarkTheme)
        collect(settingsDataStore.lang, ::collectLanguage)
        collect(settingsDataStore.distanceDimen, ::collectWindSpeed)
        collect(settingsDataStore.tempDimen, ::collectTemperature)
        collect(settingsDataStore.pressureDimen, ::collectPressure)
    }

    fun onUserInteraction(interaction: SettingsUserInteraction) {
        analytics.onUserInteraction(interaction)
        when (interaction) {
            is SettingsUserInteraction.ChangeLanguage -> onLanguage(interaction.lang)
            is SettingsUserInteraction.ChangeDarkMode -> onDarkMode(interaction.darkMode)
            is SettingsUserInteraction.ChangeDistanceDimension -> onDistanceDimen(interaction.distanceDimen)
            is SettingsUserInteraction.ChangeTemperatureDimension -> onTemperatureDimen(interaction.tempDimen)
            is SettingsUserInteraction.ChangePressureDimension -> onPressureDimen(interaction.pressureDimen)
            is SettingsUserInteraction.ShowSettingDialog -> showSettingsDialog(interaction.type)
            SettingsUserInteraction.DismissDialog -> hideDialog()
            SettingsUserInteraction.OnBackClick -> setAction(SettingsAction.NavigateBack)
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
            hideDialog()
            settingsDataStore.setDarkMode(darkMode)
        }
    }

    private fun onLanguage(lang: Lang) {
        viewModelScope.launch {
            hideDialog()
            settingsDataStore.setLanguage(lang)
        }
    }

    private fun onDistanceDimen(distanceDimen: DistanceDimen) {
        viewModelScope.launch {
            settingsDataStore.setDistanceDimension(distanceDimen)
            delay(500)
            hideDialog()
        }
    }

    private fun onTemperatureDimen(tempDimen: TempDimen) {
        viewModelScope.launch {
            settingsDataStore.setTemperatureDimension(tempDimen)
            delay(500)
            hideDialog()
        }
    }

    private fun onPressureDimen(pressureDimen: PressureDimen) {
        viewModelScope.launch {
            settingsDataStore.setPressureDimension(pressureDimen)
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

    private fun collectPressure(pressure: Pressure) = setState {
        copy(pressure = pressure)
    }

}