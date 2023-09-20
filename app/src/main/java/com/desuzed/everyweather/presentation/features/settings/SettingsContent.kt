package com.desuzed.everyweather.presentation.features.settings

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.app_update.InAppUpdateStatus
import com.desuzed.everyweather.presentation.features.settings.ui.SettingDialog
import com.desuzed.everyweather.presentation.features.settings.ui.SettingsAppUpdateContent
import com.desuzed.everyweather.presentation.features.settings.ui.SettingsMenuGroupContent
import com.desuzed.everyweather.ui.AppPreview
import com.desuzed.everyweather.ui.elements.BoldText
import com.desuzed.everyweather.ui.elements.LargeBoldText
import com.desuzed.everyweather.ui.theming.EveryweatherTheme

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
    EveryweatherTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(dimensionResource(id = R.dimen.dimen_10))
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
                                contentDescription = "",
                                tint = EveryweatherTheme.colors.textColorPrimary
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
                    items = listOf(state.lang, state.darkTheme)
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
                    items = listOf(state.tempDimen, state.windSpeed, state.pressure)
                )
                SettingsAppUpdateContent(state.updateStatus, onUserInteraction)
                SettingDialog(
                    showDialogType = state.showDialogType,
                    language = state.lang,
                    windSpeed = state.windSpeed,
                    temperature = state.tempDimen,
                    darkTheme = state.darkTheme,
                    pressure = state.pressure,
                    langDialogItems = state.langDialogItems,
                    darkModeDialogItems = state.darkModeDialogItems,
                    temperatureDialogItems = state.temperatureDialogItems,
                    distanceDialogItems = state.distanceDialogItems,
                    pressureDialogItems = state.pressureDialogItems,
                    onUserInteraction = onUserInteraction,
                    onDismiss = {
                        onUserInteraction(SettingsUserInteraction.DismissDialog)
                    }
                )

            }
        }

    }
}
