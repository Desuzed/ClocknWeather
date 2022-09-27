package com.desuzed.everyweather.analytics

import android.content.Context
import com.desuzed.everyweather.presentation.features.settings.SettingsUserInteraction

class SettingsAnalytics(context: Context) : Analytics(context = context) {

    fun onUserInteraction(interaction: SettingsUserInteraction) {
        when (interaction) {
            is SettingsUserInteraction.ChangeDarkMode -> logEvent(CHANGE_DARK_MODE)
            is SettingsUserInteraction.ChangeDistanceDimension -> logEvent(CHANGE_DISTANCE)
            is SettingsUserInteraction.ChangeLanguage -> logEvent(CHANGE_LANG)
            is SettingsUserInteraction.ChangePressureDimension -> logEvent(CHANGE_PRESSURE)
            is SettingsUserInteraction.ChangeTemperatureDimension -> logEvent(CHANGE_TEMPERATURE)
            else -> {}
        }
    }

    companion object {
        private const val CHANGE_LANG = "settings_change_lang"
        private const val CHANGE_DARK_MODE = "settings_change_dark_mode"
        private const val CHANGE_TEMPERATURE = "settings_change_temp"
        private const val CHANGE_DISTANCE = "settings_change_distance"
        private const val CHANGE_PRESSURE = "settings_change_pressure"
    }
}