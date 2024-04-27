package com.desuzed.everyweather.presentation.features.settings.ui

import androidx.compose.runtime.Composable
import com.desuzed.everyweather.domain.model.settings.DarkMode
import com.desuzed.everyweather.domain.model.settings.DistanceDimen
import com.desuzed.everyweather.domain.model.settings.Lang
import com.desuzed.everyweather.domain.model.settings.PressureDimen
import com.desuzed.everyweather.domain.model.settings.SettingsType
import com.desuzed.everyweather.domain.model.settings.TempDimen
import com.desuzed.everyweather.presentation.features.settings.SettingsUserInteraction
import com.desuzed.everyweather.presentation.ui.settings.DarkTheme
import com.desuzed.everyweather.presentation.ui.settings.Language
import com.desuzed.everyweather.presentation.ui.settings.Pressure
import com.desuzed.everyweather.presentation.ui.settings.SettingUiItem
import com.desuzed.everyweather.presentation.ui.settings.Temperature
import com.desuzed.everyweather.presentation.ui.settings.WindSpeed
import com.desuzed.everyweather.ui.elements.AppDialog

@Composable
fun SettingDialog(
    showDialogType: SettingsType?,
    language: Language,
    windSpeed: WindSpeed,
    temperature: Temperature,
    darkTheme: DarkTheme,
    pressure: Pressure,
    langDialogItems: List<SettingUiItem<Lang>>,
    darkModeDialogItems: List<SettingUiItem<DarkMode>>,
    temperatureDialogItems: List<SettingUiItem<TempDimen>>,
    distanceDialogItems: List<SettingUiItem<DistanceDimen>>,
    pressureDialogItems: List<SettingUiItem<PressureDimen>>,
    onUserInteraction: (SettingsUserInteraction) -> Unit,
    onDismiss: () -> Unit,
) {

    when (showDialogType) {
        SettingsType.LANG -> AppDialog(onDismiss = onDismiss) {
            AppDialog(onDismiss = onDismiss) {
                SettingPickerContent(
                    selectedItem = language,
                    radioOptions = langDialogItems
                ) {
                    onUserInteraction(
                        SettingsUserInteraction.ChangeLanguage(lang = it)
                    )
                }
            }
        }

        SettingsType.TEMP -> AppDialog(onDismiss = onDismiss) {
            AppDialog(onDismiss = onDismiss) {
                SettingPickerContent(
                    selectedItem = temperature,
                    radioOptions = temperatureDialogItems
                ) {
                    onUserInteraction(
                        SettingsUserInteraction.ChangeTemperatureDimension(tempDimen = it)
                    )
                }
            }
        }

        SettingsType.DISTANCE -> AppDialog(onDismiss = onDismiss) {
            AppDialog(onDismiss = onDismiss) {
                SettingPickerContent(
                    selectedItem = windSpeed,
                    radioOptions = distanceDialogItems
                ) {
                    onUserInteraction(
                        SettingsUserInteraction.ChangeDistanceDimension(distanceDimen = it)
                    )
                }
            }
        }

        SettingsType.DARK_MODE -> AppDialog(onDismiss = onDismiss) {
            AppDialog(onDismiss = onDismiss) {
                SettingPickerContent(
                    selectedItem = darkTheme,
                    radioOptions = darkModeDialogItems
                ) {
                    onUserInteraction(
                        SettingsUserInteraction.ChangeDarkMode(darkMode = it)
                    )
                }
            }
        }

        SettingsType.PRESSURE -> {
            AppDialog(onDismiss = onDismiss) {
                SettingPickerContent(
                    selectedItem = pressure,
                    radioOptions = pressureDialogItems
                ) {
                    onUserInteraction(
                        SettingsUserInteraction.ChangePressureDimension(
                            pressureDimen = it
                        )
                    )
                }
            }
        }

        null -> {}
    }
}