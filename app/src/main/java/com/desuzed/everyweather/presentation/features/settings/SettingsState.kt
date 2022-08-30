package com.desuzed.everyweather.presentation.features.settings

import com.desuzed.everyweather.domain.model.settings.*

data class SettingsState(
    val lang: Language = Language("", 0, 0),
    val darkTheme: DarkTheme = DarkTheme("", 0, 0),
    val windSpeed: WindSpeed = WindSpeed("", 0, 0),
    val tempDimen: Temperature = Temperature("", 0, 0),
    val pressure: Pressure = Pressure("", 0, 0),
    val showDialogType: SettingsType? = null,
)
