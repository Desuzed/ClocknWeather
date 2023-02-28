package com.desuzed.everyweather.presentation.features.settings

import com.desuzed.everyweather.domain.model.settings.*
import com.desuzed.everyweather.presentation.base.UserInteraction

sealed interface SettingsUserInteraction : UserInteraction {
    object DismissDialog : SettingsUserInteraction
    object OnBackClick : SettingsUserInteraction
    class ShowSettingDialog(val type: SettingsType) : SettingsUserInteraction
    class ChangeLanguage(val lang: Lang) : SettingsUserInteraction
    class ChangeDarkMode(val darkMode: DarkMode) : SettingsUserInteraction
    class ChangeDistanceDimension(val distanceDimen: DistanceDimen) : SettingsUserInteraction
    class ChangeTemperatureDimension(val tempDimen: TempDimen) : SettingsUserInteraction
    class ChangePressureDimension(val pressureDimen: PressureDimen) : SettingsUserInteraction
}