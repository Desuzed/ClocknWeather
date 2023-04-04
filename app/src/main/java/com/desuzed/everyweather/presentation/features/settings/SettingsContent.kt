package com.desuzed.everyweather.presentation.features.settings

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.app_update.InAppUpdateStatus
import com.desuzed.everyweather.domain.model.settings.*
import com.desuzed.everyweather.ui.elements.*
import com.desuzed.everyweather.ui.theming.EveryweatherTheme

@Preview(
    device = Devices.NEXUS_7,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "PreviewLocationMainContent"
)
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
                val onDismissCallback: () -> Unit = {
                    onUserInteraction(SettingsUserInteraction.DismissDialog)
                }
                when (state.showDialogType) {
                    SettingsType.LANG -> AppDialog(onDismiss = onDismissCallback) {
                        LanguagePickerContent(onUserInteraction, state.lang)
                    }
                    SettingsType.TEMP -> AppDialog(onDismiss = onDismissCallback) {
                        TemperaturePickerContent(onUserInteraction, state.tempDimen)
                    }
                    SettingsType.DISTANCE -> AppDialog(onDismiss = onDismissCallback) {
                        DistancePickerContent(onUserInteraction, state.windSpeed)
                    }
                    SettingsType.DARK_MODE -> AppDialog(onDismiss = onDismissCallback) {
                        DarkModePickerContent(onUserInteraction, state.darkTheme)
                    }
                    SettingsType.PRESSURE -> AppDialog(onDismiss = onDismissCallback) {
                        PressurePickerContent(onUserInteraction, state.pressure)
                    }
                    null -> {}
                }
            }
        }

    }
}

@Composable
fun SettingsAppUpdateContent(
    updateStatus: InAppUpdateStatus?,
    onUserInteraction: (SettingsUserInteraction) -> Unit,
) {
    if (updateStatus != null) {
        val userInteraction: SettingsUserInteraction
        val titleTextId: Int
        val buttonTextId: Int
        when (updateStatus) {
            InAppUpdateStatus.READY_TO_LAUNCH_UPDATE -> {
                userInteraction = SettingsUserInteraction.ReadyToLaunchUpdate
                titleTextId = R.string.update_available_title
                buttonTextId = R.string.update_available_update_in_background_button
            }
            InAppUpdateStatus.READY_TO_INSTALL -> {
                userInteraction = SettingsUserInteraction.ReadyToInstall
                titleTextId = R.string.update_ready_to_install_title
                buttonTextId = R.string.update_install_button
            }
        }
        MediumText(
            text = stringResource(id = titleTextId),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = dimensionResource(id = R.dimen.dimen_10),
                    end = dimensionResource(id = R.dimen.dimen_10),
                    top = dimensionResource(id = R.dimen.dimen_33),
                )
        )
        RoundedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(id = R.dimen.dimen_10)),
            onClick = {
                onUserInteraction(userInteraction)
            },
            text = stringResource(id = buttonTextId),
        )
    }
}

@Composable
fun SettingsMenuGroupContent(
    modifier: Modifier = Modifier,
    items: List<BaseSettingItem>,
    onUserInteraction: (SettingsUserInteraction) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    RoundedCardItem(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.dimen_10))
    ) {
        Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.dimen_10))) {
            items.forEach { item ->
                val onItemCLick: () -> Unit = {
                    onUserInteraction(
                        SettingsUserInteraction.ShowSettingDialog(item.type)
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(
                            horizontal = dimensionResource(id = R.dimen.dimen_10),
                            vertical = dimensionResource(id = R.dimen.dimen_8)
                        )
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = onItemCLick,
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,

                    ) {
                    Column {
                        BoldText(
                            text = stringResource(id = item.categoryStringId),
                            onClick = onItemCLick
                        )
                        MediumText(text = stringResource(item.valueStringId))
                    }
                    Icon(
                        modifier = Modifier.rotate(90f),
                        painter = painterResource(id = R.drawable.ic_arrow_24),
                        contentDescription = "",
                        tint = EveryweatherTheme.colors.textColorPrimary
                    )
                }
            }

        }
    }
}

//todo refactor
@Composable
fun DarkModePickerContent(
    onUserInteraction: (SettingsUserInteraction) -> Unit,
    selectedItem: DarkTheme,
) {
    val selectedMode = DarkMode.valueOf(selectedItem.id)
    val radioOptions = DarkMode.values().toList()
    val indexOfSelected = radioOptions.indexOf(selectedMode)
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[indexOfSelected]) }
    val onPickMode: (DarkMode) -> Unit = { darkMode ->
        onOptionSelected(darkMode)
        onUserInteraction(SettingsUserInteraction.ChangeDarkMode(darkMode = darkMode))
    }
    Column(modifier = Modifier.padding(10.dp)) {
        radioOptions.forEach { darkMode ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        role = Role.RadioButton,
                        selected = (darkMode == selectedOption),
                        onClick = {
                            onPickMode(darkMode)
                        }
                    )
                    .padding(vertical = dimensionResource(id = R.dimen.dimen_4)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppRadioButton(isSelected = darkMode == selectedOption) {
                    onPickMode(darkMode)
                }
                val textId = when (darkMode) {
                    DarkMode.ON -> R.string.on
                    DarkMode.OFF -> R.string.off
                    DarkMode.SYSTEM -> R.string.system
                }
                MediumText(text = stringResource(id = textId))
            }
        }
    }
}

@Composable
fun LanguagePickerContent(
    onUserInteraction: (SettingsUserInteraction) -> Unit,
    selectedItem: Language,
) {
    val selectedLang = Lang.valueOf(selectedItem.id)
    val radioOptions = Lang.values().toList()
    val indexOfSelected = radioOptions.indexOf(selectedLang)
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[indexOfSelected]) }
    val onPickMode: (Lang) -> Unit = { language ->
        onOptionSelected(language)
        onUserInteraction(SettingsUserInteraction.ChangeLanguage(lang = language))
    }
    Column(modifier = Modifier.padding(10.dp)) {
        radioOptions.forEach { language ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        role = Role.RadioButton,
                        selected = (language == selectedOption),
                        onClick = {
                            onPickMode(language)
                        }
                    )
                    .padding(vertical = dimensionResource(id = R.dimen.dimen_4)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppRadioButton(isSelected = language == selectedOption) {
                    onPickMode(language)
                }
                val textId = when (language) {
                    Lang.EN -> R.string.english
                    Lang.RU -> R.string.russian
                }
                MediumText(text = stringResource(id = textId))
            }
        }
    }
}

@Composable
fun TemperaturePickerContent(
    onUserInteraction: (SettingsUserInteraction) -> Unit,
    selectedItem: Temperature,
) {
    val selectedTemp = TempDimen.valueOf(selectedItem.id)
    val radioOptions = TempDimen.values().toList()
    val indexOfSelected = radioOptions.indexOf(selectedTemp)
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[indexOfSelected]) }
    val onPickMode: (TempDimen) -> Unit = { temperatureDimension ->
        onOptionSelected(temperatureDimension)
        onUserInteraction(SettingsUserInteraction.ChangeTemperatureDimension(tempDimen = temperatureDimension))
    }
    Column(modifier = Modifier.padding(10.dp)) {
        radioOptions.forEach { temperatureDimension ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        role = Role.RadioButton,
                        selected = (temperatureDimension == selectedOption),
                        onClick = {
                            onPickMode(temperatureDimension)
                        }
                    )
                    .padding(vertical = dimensionResource(id = R.dimen.dimen_4)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppRadioButton(isSelected = temperatureDimension == selectedOption) {
                    onPickMode(temperatureDimension)
                }
                val textId = when (temperatureDimension) {
                    TempDimen.FAHRENHEIT -> R.string.fahrenheit
                    TempDimen.CELCIUS -> R.string.celcius
                }
                MediumText(text = stringResource(id = textId))
            }
        }
    }
}

@Composable
fun DistancePickerContent(
    onUserInteraction: (SettingsUserInteraction) -> Unit,
    selectedItem: WindSpeed,
) {
    val selectedDist = DistanceDimen.valueOf(selectedItem.id)
    val radioOptions = DistanceDimen.values().toList()
    val indexOfSelected = radioOptions.indexOf(selectedDist)
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[indexOfSelected]) }
    val onPickMode: (DistanceDimen) -> Unit = { distanceDimension ->
        onOptionSelected(distanceDimension)
        onUserInteraction(SettingsUserInteraction.ChangeDistanceDimension(distanceDimen = distanceDimension))
    }
    Column(modifier = Modifier.padding(10.dp)) {
        radioOptions.forEach { distanceDimension ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        role = Role.RadioButton,
                        selected = (distanceDimension == selectedOption),
                        onClick = {
                            onPickMode(distanceDimension)
                        }
                    )
                    .padding(vertical = dimensionResource(id = R.dimen.dimen_4)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppRadioButton(isSelected = distanceDimension == selectedOption) {
                    onPickMode(distanceDimension)
                }
                val textId = when (distanceDimension) {
                    DistanceDimen.IMPERIAL -> R.string.mph
                    DistanceDimen.METRIC_KMH -> R.string.kmh
                    DistanceDimen.METRIC_MS -> R.string.ms
                }
                MediumText(text = stringResource(id = textId))
            }
        }
    }
}

@Composable
fun PressurePickerContent(
    onUserInteraction: (SettingsUserInteraction) -> Unit,
    selectedItem: Pressure,
) {
    val selectedPressure = PressureDimen.valueOf(selectedItem.id)
    val radioOptions = PressureDimen.values().toList()
    val indexOfSelected = radioOptions.indexOf(selectedPressure)
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[indexOfSelected]) }
    val onPickMode: (PressureDimen) -> Unit = { pressureDimen ->
        onOptionSelected(pressureDimen)
        onUserInteraction(SettingsUserInteraction.ChangePressureDimension(pressureDimen = pressureDimen))
    }
    Column(modifier = Modifier.padding(10.dp)) {
        radioOptions.forEach { pressureDimen ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        role = Role.RadioButton,
                        selected = (pressureDimen == selectedOption),
                        onClick = {
                            onPickMode(pressureDimen)
                        }
                    )
                    .padding(vertical = dimensionResource(id = R.dimen.dimen_4)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppRadioButton(isSelected = pressureDimen == selectedOption) {
                    onPickMode(pressureDimen)
                }
                val textId = when (pressureDimen) {
                    PressureDimen.MILLIBAR -> R.string.mb
                    PressureDimen.MILLIMETERS -> R.string.mmhg
                    PressureDimen.INCHES -> R.string.inch
                }
                MediumText(text = stringResource(id = textId))
            }
        }
    }
}
