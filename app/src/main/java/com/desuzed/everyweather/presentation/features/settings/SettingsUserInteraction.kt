package com.desuzed.everyweather.presentation.features.settings

import com.desuzed.everyweather.domain.model.settings.*

sealed interface SettingsUserInteraction {
    class ShowSettingDialog(val type: SettingsType) : SettingsUserInteraction
    object DismissDialog : SettingsUserInteraction
    class ChangeLanguage(val lang: Lang) : SettingsUserInteraction
    class ChangeDarkMode(val darkMode: DarkMode) : SettingsUserInteraction
    class ChangeDistanceDimension(val distanceDimen: DistanceDimen) : SettingsUserInteraction
    class ChangeTemperatureDimension(val tempDimen: TempDimen) : SettingsUserInteraction
}