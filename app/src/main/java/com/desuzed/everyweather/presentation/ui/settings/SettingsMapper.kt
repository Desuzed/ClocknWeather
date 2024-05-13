package com.desuzed.everyweather.presentation.ui.settings

import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.settings.DarkMode
import com.desuzed.everyweather.domain.model.settings.DistanceDimen
import com.desuzed.everyweather.domain.model.settings.Lang
import com.desuzed.everyweather.domain.model.settings.PressureDimen
import com.desuzed.everyweather.domain.model.settings.TempDimen

object SettingsMapper {

    fun getSettingsUiParams(
        selectedLang: Lang,
        selectedMode: DarkMode,
        selectedDistanceDimen: DistanceDimen,
        selectedTempDimen: TempDimen,
        selectedPressureDimen: PressureDimen,
        distanceDimenList: List<DistanceDimen>,
        tempList: List<TempDimen>,
        pressureList: List<PressureDimen>,
        langList: List<Lang>,
        darkModeList: List<DarkMode>,
    ) = SettingsUiParams(
        weatherUiList = getWeatherUiSettings(distanceDimenList, tempList, pressureList),
        systemSettingsList = getSystemUiSettings(langList, darkModeList),
        selectedLang = getSelectedLanguage(selectedLang),
        selectedMode = getSelectedMode(selectedMode),
        selectedPressure = getSelectedPressure(selectedPressureDimen),
        selectedTemp = getSelectedTemperature(selectedTempDimen),
        selectedDistance = getSelectedWindSpeed(selectedDistanceDimen),
    )

    fun getSelectedPressure(selectedPressureDimen: PressureDimen): Pressure {
        val pressureValueId = when (selectedPressureDimen) {
            PressureDimen.MILLIMETERS -> R.string.mmhg
            PressureDimen.MILLIBAR -> R.string.mb
            PressureDimen.INCHES -> R.string.inch
        }
        return Pressure(
            id = selectedPressureDimen.dimensionName,
            categoryStringId = R.string.pressure,
            valueStringId = pressureValueId,
        )
    }

    fun getSelectedWindSpeed(selectedDistance: DistanceDimen): WindSpeed {
        val distValueId = when (selectedDistance) {
            DistanceDimen.METRIC_KMH -> R.string.kmh
            DistanceDimen.IMPERIAL -> R.string.mph
            DistanceDimen.METRIC_MS -> R.string.ms
        }
        return WindSpeed(
            id = selectedDistance.dimensionName,
            categoryStringId = R.string.distance_dimension,
            valueStringId = distValueId,
        )
    }

    private fun getSelectedLanguage(lang: Lang): Language {
        val langValue = when (lang) {
            Lang.RU -> R.string.russian
            Lang.EN -> R.string.english
        }
        return Language(
            id = lang.lang,
            categoryStringId = R.string.language,
            valueStringId = langValue,
        )
    }

    private fun getSelectedTemperature(selectedTemperature: TempDimen): Temperature {
        val tempValueId = when (selectedTemperature) {
            TempDimen.CELCIUS -> R.string.celcius
            TempDimen.FAHRENHEIT -> R.string.fahrenheit
        }
        return Temperature(
            id = selectedTemperature.dimensionName,
            categoryStringId = R.string.temperature_dimension,
            valueStringId = tempValueId,
        )
    }

    private fun getSelectedMode(mode: DarkMode): DarkTheme {
        val modeValue = when (mode) {
            DarkMode.SYSTEM -> R.string.system
            DarkMode.ON -> R.string.on
            DarkMode.OFF -> R.string.off
        }
        return DarkTheme(
            id = mode.mode,
            categoryStringId = R.string.dark_mode,
            valueStringId = modeValue,
        )
    }

    private fun getWeatherUiSettings(
        distanceDimenList: List<DistanceDimen>,
        tempList: List<TempDimen>,
        pressureList: List<PressureDimen>
    ) = WeatherSettingsUiList(
        distanceSettingsList = getDistanceItemsList(distanceDimenList),
        temperatureSettingsList = getTemperatureItemsList(tempList),
        pressureSettingsList = getPressureItemsList(pressureList),
    )

    private fun getSystemUiSettings(
        langList: List<Lang>,
        darkModeList: List<DarkMode>,
    ) = SystemSettingsUiList(
        languageList = getLanguageItemsList(langList),
        darkModeList = getDarkModeItemsList(darkModeList),
    )

    private fun getDistanceItemsList(distanceDimenList: List<DistanceDimen>): List<SettingUiItem<DistanceDimen>> {
        return distanceDimenList.map {
            when (it) {
                DistanceDimen.METRIC_KMH -> SettingUiItem(it, R.string.kmh)
                DistanceDimen.METRIC_MS -> SettingUiItem(it, R.string.ms)
                DistanceDimen.IMPERIAL -> SettingUiItem(it, R.string.mph)
            }
        }
    }

    private fun getTemperatureItemsList(tempList: List<TempDimen>): List<SettingUiItem<TempDimen>> {
        return tempList.map {
            when (it) {
                TempDimen.FAHRENHEIT -> SettingUiItem(it, R.string.fahrenheit)
                TempDimen.CELCIUS -> SettingUiItem(it, R.string.celcius)
            }
        }
    }

    private fun getPressureItemsList(pressureList: List<PressureDimen>): List<SettingUiItem<PressureDimen>> {
        return pressureList.map {
            when (it) {
                PressureDimen.MILLIBAR -> SettingUiItem(it, R.string.mb)
                PressureDimen.MILLIMETERS -> SettingUiItem(it, R.string.mmhg)
                PressureDimen.INCHES -> SettingUiItem(it, R.string.inch)
            }
        }
    }

    private fun getLanguageItemsList(langList: List<Lang>): List<SettingUiItem<Lang>> {
        return langList.map {
            when (it) {
                Lang.RU -> SettingUiItem(it, R.string.russian)
                Lang.EN -> SettingUiItem(it, R.string.english)
            }
        }
    }

    private fun getDarkModeItemsList(darkModeList: List<DarkMode>): List<SettingUiItem<DarkMode>> {
        return darkModeList.map {
            when (it) {
                DarkMode.ON -> SettingUiItem(it, R.string.on)
                DarkMode.OFF -> SettingUiItem(it, R.string.off)
                DarkMode.SYSTEM -> SettingUiItem(it, R.string.system)
            }
        }
    }
}