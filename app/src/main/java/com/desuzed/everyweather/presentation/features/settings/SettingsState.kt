package com.desuzed.everyweather.presentation.features.settings

import com.desuzed.everyweather.domain.model.app_update.InAppUpdateStatus
import com.desuzed.everyweather.domain.model.settings.DarkMode
import com.desuzed.everyweather.domain.model.settings.DarkTheme
import com.desuzed.everyweather.domain.model.settings.DistanceDimen
import com.desuzed.everyweather.domain.model.settings.Lang
import com.desuzed.everyweather.domain.model.settings.Language
import com.desuzed.everyweather.domain.model.settings.Pressure
import com.desuzed.everyweather.domain.model.settings.PressureDimen
import com.desuzed.everyweather.domain.model.settings.SettingUiItem
import com.desuzed.everyweather.domain.model.settings.SettingsType
import com.desuzed.everyweather.domain.model.settings.TempDimen
import com.desuzed.everyweather.domain.model.settings.Temperature
import com.desuzed.everyweather.domain.model.settings.WindSpeed

data class SettingsState(
    val lang: Language = Language("", 0, 0),
    val darkTheme: DarkTheme = DarkTheme("", 0, 0),
    val windSpeed: WindSpeed = WindSpeed("", 0, 0),
    val tempDimen: Temperature = Temperature("", 0, 0),
    val pressure: Pressure = Pressure("", 0, 0),
    val showDialogType: SettingsType? = null,
    val langDialogItems: List<SettingUiItem<Lang>> = emptyList(),
    val darkModeDialogItems: List<SettingUiItem<DarkMode>> = emptyList(),
    val temperatureDialogItems: List<SettingUiItem<TempDimen>> = emptyList(),
    val distanceDialogItems: List<SettingUiItem<DistanceDimen>> = emptyList(),
    val pressureDialogItems: List<SettingUiItem<PressureDimen>> = emptyList(),
    val updateStatus: InAppUpdateStatus? = null,
)
