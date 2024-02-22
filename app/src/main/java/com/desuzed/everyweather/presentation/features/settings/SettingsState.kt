package com.desuzed.everyweather.presentation.features.settings

import com.desuzed.everyweather.R
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
    val lang: Language = Language(
        id = Lang.EN.lang,
        categoryStringId = R.string.language,
        valueStringId = R.string.english,
    ),
    val darkTheme: DarkTheme = DarkTheme(DarkMode.SYSTEM.mode, R.string.dark_mode, R.string.system),
    val windSpeed: WindSpeed = WindSpeed(
        id = DistanceDimen.METRIC_KMH.dimensionName,
        categoryStringId = R.string.wind,
        valueStringId = R.string.kmh,
    ),
    val tempDimen: Temperature = Temperature(
        id = TempDimen.CELCIUS.dimensionName,
        categoryStringId = R.string.temperature_dimension,
        valueStringId = R.string.celcius,
    ),
    val pressure: Pressure = Pressure(
        id = PressureDimen.MILLIMETERS.dimensionName,
        categoryStringId = R.string.pressure,
        valueStringId = R.string.mb,
    ),
    val showDialogType: SettingsType? = null,
    val langDialogItems: List<SettingUiItem<Lang>> = emptyList(),
    val darkModeDialogItems: List<SettingUiItem<DarkMode>> = emptyList(),
    val temperatureDialogItems: List<SettingUiItem<TempDimen>> = emptyList(),
    val distanceDialogItems: List<SettingUiItem<DistanceDimen>> = emptyList(),
    val pressureDialogItems: List<SettingUiItem<PressureDimen>> = emptyList(),
    val updateStatus: InAppUpdateStatus? = null,
)
