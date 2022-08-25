package com.desuzed.everyweather.presentation.features.settings

import com.desuzed.everyweather.domain.model.settings.*

sealed interface SettingsUserInteraction {
    class ShowSettingDialog(val type: SettingsType) : SettingsUserInteraction
    object DismissDialog : SettingsUserInteraction
    class ChangeLanguage(val language: Language) : SettingsUserInteraction
    class ChangeDarkMode(val darkMode: DarkMode) : SettingsUserInteraction
    class ChangeDistanceDimension(val distanceDimension: DistanceDimension) :
        SettingsUserInteraction

    class ChangeTemperatureDimension(val temperatureDimension: TemperatureDimension) :
        SettingsUserInteraction
}