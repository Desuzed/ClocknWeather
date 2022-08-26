package com.desuzed.everyweather.presentation.features.settings

import com.desuzed.everyweather.domain.model.settings.DarkMode
import com.desuzed.everyweather.domain.model.settings.Lang


sealed interface SettingsAction{
    class ChangeLanguage(val lang: Lang) : SettingsAction
    class ChangeDarkMode(val mode: DarkMode) : SettingsAction
    object NavigateBack : SettingsAction//todo navigateBack btn + location fragment
}