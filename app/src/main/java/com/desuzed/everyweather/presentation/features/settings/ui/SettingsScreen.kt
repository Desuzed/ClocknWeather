package com.desuzed.everyweather.presentation.features.settings.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.desuzed.everyweather.presentation.features.settings.SettingsEffect
import com.desuzed.everyweather.presentation.features.settings.SettingsState
import com.desuzed.everyweather.presentation.features.settings.SettingsUserInteraction
import com.desuzed.everyweather.presentation.features.settings.SettingsViewModel
import com.desuzed.everyweather.presentation.ui.settings.SettingsMapper
import com.desuzed.everyweather.ui.AppPreview
import com.desuzed.everyweather.ui.extensions.CollectAction
import com.desuzed.everyweather.ui.extensions.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@AppPreview
@Composable
private fun Preview() {
    SettingsScreen(
        navController = NavController(LocalContext.current)
    )
}

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle(initialState = SettingsState())
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
    CollectAction(source = viewModel.sideEffect) {
        when (it) {
            SettingsEffect.NavigateBack -> navController.popBackStack()
            is SettingsEffect.ShowReadyToInstallDialog -> TODO()
            is SettingsEffect.ShowUpdateDialog -> TODO()
        }
    }
    SettingsScreenBody(
        settingsParams = settingsParams,
        updateStatus = state.updateStatus,
        onUserInteraction = viewModel::onUserInteraction,
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
        onUserInteraction = viewModel::onUserInteraction,
        onDismiss = {
            viewModel.onUserInteraction(SettingsUserInteraction.DismissDialog)
        }
    )
}
