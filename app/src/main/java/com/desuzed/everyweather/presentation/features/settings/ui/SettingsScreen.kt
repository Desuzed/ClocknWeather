package com.desuzed.everyweather.presentation.features.settings.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.desuzed.everyweather.presentation.features.settings.SettingsAction
import com.desuzed.everyweather.presentation.features.settings.SettingsState
import com.desuzed.everyweather.presentation.ui.settings.SettingsMapper
import com.desuzed.everyweather.ui.AppPreview

@AppPreview
@Composable
private fun Preview() {
    SettingsScreen(
        state = SettingsState(),
        onAction = {},
    )
}

@Composable
fun SettingsScreen(
    state: SettingsState,
    onAction: (SettingsAction) -> Unit,
) {
    val settingsParams = remember(
        state.selectedLang,
        state.selectedMode,
        state.selectedPressure,
        state.selectedDistanceDimen,
        state.selectedTempDimen,
    ) {
        SettingsMapper.getSettingsUiParams(
            distanceDimenList = state.distanceDialogItems,
            tempList = state.temperatureDialogItems,
            pressureList = state.pressureDialogItems,
            langList = state.langDialogItems,
            darkModeList = state.darkModeDialogItems,
            selectedMode = state.selectedMode,
            selectedLang = state.selectedLang,
            selectedDistanceDimen = state.selectedDistanceDimen,
            selectedTempDimen = state.selectedTempDimen,
            selectedPressureDimen = state.selectedPressure,
        )
    }
    SettingsScreenBody(
        settingsParams = settingsParams,
        updateStatus = state.updateStatus,
        onAction = onAction,
    )
    SettingDialog(
        showDialogType = state.showDialogType,
        language = settingsParams.selectedLang,
        windSpeed = settingsParams.selectedDistance,
        temperature = settingsParams.selectedTemp,
        darkTheme = settingsParams.selectedMode,
        pressure = settingsParams.selectedPressure,
        langDialogItems = settingsParams.systemSettingsList.languageList,
        darkModeDialogItems = settingsParams.systemSettingsList.darkModeList,
        temperatureDialogItems = settingsParams.weatherUiList.temperatureSettingsList,
        distanceDialogItems = settingsParams.weatherUiList.distanceSettingsList,
        pressureDialogItems = settingsParams.weatherUiList.pressureSettingsList,
        onAction = onAction,
        onDismiss = {
            onAction(SettingsAction.DismissDialog)
        }
    )
}
