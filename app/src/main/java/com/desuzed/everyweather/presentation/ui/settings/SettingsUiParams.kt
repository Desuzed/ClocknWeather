package com.desuzed.everyweather.presentation.ui.settings

class SettingsUiParams(
    val weatherUiList: WeatherSettingsUiList,
    val systemSettingsList: SystemSettingsUiList,
    val selectedLang: Language,
    val selectedMode: DarkTheme,
    val selectedDistance: WindSpeed,
    val selectedTemp: Temperature,
    val selectedPressure: Pressure,
)