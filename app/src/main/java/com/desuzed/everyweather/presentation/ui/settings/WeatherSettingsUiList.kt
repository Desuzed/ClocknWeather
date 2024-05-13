package com.desuzed.everyweather.presentation.ui.settings

import com.desuzed.everyweather.domain.model.settings.DistanceDimen
import com.desuzed.everyweather.domain.model.settings.PressureDimen
import com.desuzed.everyweather.domain.model.settings.TempDimen

class WeatherSettingsUiList(
    val distanceSettingsList: List<SettingUiItem<DistanceDimen>>,
    val temperatureSettingsList: List<SettingUiItem<TempDimen>>,
    val pressureSettingsList: List<SettingUiItem<PressureDimen>>,
)