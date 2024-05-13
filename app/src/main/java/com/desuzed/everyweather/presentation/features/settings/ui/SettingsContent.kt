package com.desuzed.everyweather.presentation.features.settings.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.app_update.InAppUpdateStatus
import com.desuzed.everyweather.presentation.features.settings.SettingsState
import com.desuzed.everyweather.presentation.features.settings.SettingsUserInteraction
import com.desuzed.everyweather.presentation.ui.settings.SettingsMapper
import com.desuzed.everyweather.ui.AppPreview
import com.desuzed.everyweather.ui.elements.BoldText
import com.desuzed.everyweather.ui.elements.GradientBox
import com.desuzed.everyweather.ui.elements.LargeBoldText
import com.desuzed.everyweather.ui.theming.EveryweatherTheme
import com.desuzed.everyweather.util.Constants.EMPTY_STRING

@AppPreview
@Composable
private fun PreviewSettingsContent() {
    SettingsContent(
        state = SettingsState(updateStatus = InAppUpdateStatus.READY_TO_INSTALL),
        onUserInteraction = {},
    )
}

@Composable
fun SettingsContent(
    state: SettingsState,
    onUserInteraction: (SettingsUserInteraction) -> Unit,
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
    EveryweatherTheme {
        GradientBox(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(dimensionResource(id = R.dimen.dimen_10)),
            colors = EveryweatherTheme.colors.primaryBackground,
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
            ) {
                Box(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(vertical = dimensionResource(id = R.dimen.dimen_10))
                ) {
                    LargeBoldText(
                        text = stringResource(id = R.string.settings),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    IconButton(
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.dimen_34))
                            .padding(start = dimensionResource(id = R.dimen.dimen_10)),
                        onClick = { onUserInteraction(SettingsUserInteraction.OnBackClick) },
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_round_arrow_back),
                                contentDescription = EMPTY_STRING,
                                tint = EveryweatherTheme.colors.onBackgroundPrimary
                            )
                        }
                    )
                }
                BoldText(
                    text = stringResource(id = R.string.app_settings),
                    modifier = Modifier.padding(
                        top = dimensionResource(id = R.dimen.dimen_10),
                        start = dimensionResource(id = R.dimen.dimen_10)
                    )
                )
                SettingsMenuGroupContent(
                    onUserInteraction = onUserInteraction,
                    items = listOf(
                        settingsParams.selectedLang,
                        settingsParams.selectedMode,
                    )
                )
                BoldText(
                    text = stringResource(id = R.string.dimension_settings),
                    modifier = Modifier.padding(
                        top = dimensionResource(id = R.dimen.dimen_10),
                        start = dimensionResource(id = R.dimen.dimen_10)
                    )
                )
                SettingsMenuGroupContent(
                    onUserInteraction = onUserInteraction,
                    items = listOf(
                        settingsParams.selectedTemp,
                        settingsParams.selectedDistance,
                        settingsParams.selectedPressure,
                    )
                )
                SettingsAppUpdateContent(state.updateStatus, onUserInteraction)
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
                    onUserInteraction = onUserInteraction,
                    onDismiss = {
                        onUserInteraction(SettingsUserInteraction.DismissDialog)
                    }
                )

            }
        }

    }
}
