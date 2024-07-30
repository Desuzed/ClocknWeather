package com.desuzed.everyweather.presentation.features.settings

import com.desuzed.everyweather.domain.model.settings.DarkMode
import com.desuzed.everyweather.domain.model.settings.DistanceDimen
import com.desuzed.everyweather.domain.model.settings.Lang
import com.desuzed.everyweather.domain.model.settings.PressureDimen
import com.desuzed.everyweather.domain.model.settings.SettingsType
import com.desuzed.everyweather.domain.model.settings.TempDimen
import com.desuzed.everyweather.presentation.base.Action

sealed interface SettingsAction : Action {
    data object DismissDialog : SettingsAction
    data object OnBackClick : SettingsAction
    class ShowSettingDialog(val type: SettingsType) : SettingsAction
    class ChangeLanguage(val lang: Lang) : SettingsAction
    class ChangeDarkMode(val darkMode: DarkMode) : SettingsAction
    class ChangeDistanceDimension(val distanceDimen: DistanceDimen) : SettingsAction
    class ChangeTemperatureDimension(val tempDimen: TempDimen) : SettingsAction
    class ChangePressureDimension(val pressureDimen: PressureDimen) : SettingsAction
    data object ReadyToLaunchUpdate : SettingsAction
    data object ReadyToInstall : SettingsAction
}