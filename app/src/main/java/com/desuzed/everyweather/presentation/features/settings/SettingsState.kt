package com.desuzed.everyweather.presentation.features.settings

import com.desuzed.everyweather.domain.model.settings.*

data class SettingsState(
    val lang: Language = Language("", "", ""),
    val darkTheme: DarkTheme = DarkTheme("", "", ""),
    val windSpeed: WindSpeed = WindSpeed("", "", ""),
    val tempDimen: Temperature = Temperature("", "", ""),
    val showDialogType: SettingsType? = null,
)
