package com.desuzed.everyweather.presentation.features.settings

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.desuzed.everyweather.data.repository.local.SettingsRepository
import com.desuzed.everyweather.domain.model.settings.*
import com.desuzed.everyweather.presentation.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
) :
    BaseViewModel<SettingsState, SettingsAction>(SettingsState()) {

    init {

    }

    fun onUserInteraction(interaction: SettingsUserInteraction) {
        when (interaction) {
            is SettingsUserInteraction.ChangeLanguage -> onLanguage(interaction.language)
            is SettingsUserInteraction.ChangeDarkMode -> onDarkMode(interaction.darkMode)
            is SettingsUserInteraction.ChangeDistanceDimension -> onDistanceDimen(interaction.distanceDimension)
            is SettingsUserInteraction.ChangeTemperatureDimension -> onTemperatureDimen(interaction.temperatureDimension)
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
            Log.i("ChangeDarkMode", "onUserInteraction: ${darkMode} ")
            setState { copy(darkMode = darkMode) }
            delay(1000)
            hideDialog()
        }
    }

    private fun onLanguage(language: Language) {
        viewModelScope.launch {
            Log.i("ChangeLang", "onUserInteraction: ${language} ")
            setState { copy(language = language) }
            delay(1000)
            hideDialog()
        }
    }

    private fun onDistanceDimen(distanceDimension: DistanceDimension) {
        viewModelScope.launch {
            Log.i("ChangeDistance", "onUserInteraction: ${distanceDimension} ")
            setState { copy(distanceDimension = distanceDimension) }
            delay(1000)
            hideDialog()
        }
    }

    private fun onTemperatureDimen(temperatureDimension: TemperatureDimension) {
        viewModelScope.launch {
            Log.i("ChangeTemper", "onUserInteraction: ${temperatureDimension} ")
            setState { copy(temperatureDimension = temperatureDimension) }
            delay(1000)
            hideDialog()
        }
    }
}