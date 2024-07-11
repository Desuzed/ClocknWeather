package com.desuzed.everyweather.presentation.features.settings

import com.desuzed.everyweather.domain.model.app_update.InAppUpdateStatus
import com.desuzed.everyweather.domain.model.settings.DarkMode
import com.desuzed.everyweather.domain.model.settings.DistanceDimen
import com.desuzed.everyweather.domain.model.settings.Lang
import com.desuzed.everyweather.domain.model.settings.PressureDimen
import com.desuzed.everyweather.domain.model.settings.SettingsType
import com.desuzed.everyweather.domain.model.settings.TempDimen
import com.desuzed.everyweather.presentation.base.State

data class SettingsState(
    val selectedLang: Lang = Lang.EN,
    val selectedMode: DarkMode = DarkMode.SYSTEM,
    val selectedDistanceDimen: DistanceDimen = DistanceDimen.METRIC_KMH,
    val selectedTempDimen: TempDimen = TempDimen.CELCIUS,
    val selectedPressure: PressureDimen = PressureDimen.MILLIMETERS,
    val showDialogType: SettingsType? = null,
    val langDialogItems: List<Lang> = Lang.entries,
    val darkModeDialogItems: List<DarkMode> = DarkMode.entries,
    val temperatureDialogItems: List<TempDimen> = TempDimen.entries,
    val distanceDialogItems: List<DistanceDimen> = DistanceDimen.entries,
    val pressureDialogItems: List<PressureDimen> = PressureDimen.entries,
    val updateStatus: InAppUpdateStatus? = null,
) : State
