package com.desuzed.everyweather.presentation.features.settings

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.settings.*
import com.desuzed.everyweather.presentation.ui.base.BaseSettingItem
import com.desuzed.everyweather.presentation.ui.settings.DarkModeItem
import com.desuzed.everyweather.presentation.ui.settings.DistanceItem
import com.desuzed.everyweather.presentation.ui.settings.LanguageItem
import com.desuzed.everyweather.presentation.ui.settings.TemperatureItem
import com.desuzed.everyweather.ui.elements.*
import com.desuzed.everyweather.ui.theming.EveryweatherTheme

@Preview(
    showBackground = true,
    widthDp = 400,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "PreviewLocationMainContent"
)
@Composable
private fun PreviewWeatherMainContent() {
    SettingsContent(
        state = SettingsState(),
        onUserInteraction = {},
    )
}

//todo contact to developer
//todo powered by
//todo locationHandling
//todo change theme?????
//todo notifications
@Composable
fun SettingsContent(
    state: SettingsState,
    onUserInteraction: (SettingsUserInteraction) -> Unit,
) {
    EveryweatherTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = dimensionResource(id = R.dimen.dimen_20),
                    start = dimensionResource(id = R.dimen.dimen_4),
                    end = dimensionResource(id = R.dimen.dimen_4),
                    bottom = dimensionResource(id = R.dimen.dimen_8),
                )
        ) {
            val res = LocalContext.current.resources
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                BoldText(text = stringResource(id = R.string.app_settings))
                SettingsMenuGroupContent(
                    onUserInteraction = onUserInteraction,
                    items = listOf(LanguageItem(res), DarkModeItem(res))
                )
                BoldText(text = stringResource(id = R.string.dimension_settings))
                SettingsMenuGroupContent(
                    onUserInteraction = onUserInteraction,
                    items = listOf(TemperatureItem(res), DistanceItem(res))
                )
                val onDismissCallback: () -> Unit = {
                    onUserInteraction(SettingsUserInteraction.DismissDialog)
                }
                when (state.showDialogType) {
                    SettingsType.LANG -> AppDialog(onDismiss = onDismissCallback) {
                        LanguagePickerContent(onUserInteraction, state.language)
                    }
                    SettingsType.TEMP -> AppDialog(onDismiss = onDismissCallback) {
                        TemperaturePickerContent(onUserInteraction, state.temperatureDimension)
                    }
                    SettingsType.DISTANCE -> AppDialog(onDismiss = onDismissCallback) {
                        DistancePickerContent(onUserInteraction, state.distanceDimension)
                    }
                    SettingsType.DARK_MODE -> AppDialog(onDismiss = onDismissCallback) {
                        DarkModePickerContent(onUserInteraction, state.darkMode)
                    }
                    null -> {}
                }
            }
        }

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
        Column(modifier = Modifier.padding(10.dp)) {
            items.forEach { item ->
                val onItemCLick: () -> Unit = {
                    onUserInteraction(
                        SettingsUserInteraction.ShowSettingDialog(item.type)
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(
                            horizontal = dimensionResource(id = R.dimen.dimen_8),
                            vertical = dimensionResource(id = R.dimen.dimen_4)
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
                        BoldText(text = item.category, onClick = onItemCLick)
                        MediumText(text = item.name)
                    }
                    Icon(
                        modifier = Modifier.rotate(90f),
                        painter = painterResource(id = R.drawable.ic_arrow_24),
                        contentDescription = "",
                        tint = EveryweatherTheme.colors.textColorPrimary//todo color
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
    selectedItem: DarkMode,
) {
    val radioOptions = DarkMode.values().toList()
    val indexOfSelected = radioOptions.indexOf(selectedItem)
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[indexOfSelected]) }
    val onPickMode: (DarkMode) -> Unit = { darkMode ->
        onOptionSelected(darkMode)
        onUserInteraction(SettingsUserInteraction.ChangeDarkMode(darkMode = darkMode))
    }
    Column {
        radioOptions.forEach { darkMode ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        role = Role.RadioButton,
                        selected = (darkMode == selectedOption),
                        onClick = {
                            onPickMode(darkMode)
                        }
                    )
                    .padding(horizontal = 16.dp)
            ) {
                AppRadioButton(isSelected = darkMode == selectedOption) {
                    onPickMode(darkMode)
                }
                MediumText(text = darkMode.mode)
            }
        }
    }
}

@Composable
fun LanguagePickerContent(
    onUserInteraction: (SettingsUserInteraction) -> Unit,
    selectedItem: Language,
) {
    val radioOptions = Language.values().toList()
    val indexOfSelected = radioOptions.indexOf(selectedItem)
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[indexOfSelected]) }
    val onPickMode: (Language) -> Unit = { language ->
        onOptionSelected(language)
        onUserInteraction(SettingsUserInteraction.ChangeLanguage(language = language))
    }
    Column {
        radioOptions.forEach { language ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        role = Role.RadioButton,
                        selected = (language == selectedOption),
                        onClick = {
                            onPickMode(language)
                        }
                    )
                    .padding(horizontal = 16.dp)
            ) {
                AppRadioButton(isSelected = language == selectedOption) {
                    onPickMode(language)
                }
                MediumText(text = language.lang)
            }
        }
    }
}

@Composable
fun TemperaturePickerContent(
    onUserInteraction: (SettingsUserInteraction) -> Unit,
    selectedItem: TemperatureDimension,
) {
    val radioOptions = TemperatureDimension.values().toList()
    val indexOfSelected = radioOptions.indexOf(selectedItem)
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[indexOfSelected]) }
    val onPickMode: (TemperatureDimension) -> Unit = { temperatureDimension ->
        onOptionSelected(temperatureDimension)
        onUserInteraction(SettingsUserInteraction.ChangeTemperatureDimension(temperatureDimension = temperatureDimension))
    }
    Column {
        radioOptions.forEach { temperatureDimension ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        role = Role.RadioButton,
                        selected = (temperatureDimension == selectedOption),
                        onClick = {
                            onPickMode(temperatureDimension)
                        }
                    )
                    .padding(horizontal = 16.dp)
            ) {
                AppRadioButton(isSelected = temperatureDimension == selectedOption) {
                    onPickMode(temperatureDimension)
                }
                MediumText(text = temperatureDimension.dimensionName)
            }
        }
    }
}

@Composable
fun DistancePickerContent(
    onUserInteraction: (SettingsUserInteraction) -> Unit,
    selectedItem: DistanceDimension,
) {
    val radioOptions = DistanceDimension.values().toList()
    val indexOfSelected = radioOptions.indexOf(selectedItem)
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[indexOfSelected]) }
    val onPickMode: (DistanceDimension) -> Unit = { distanceDimension ->
        onOptionSelected(distanceDimension)
        onUserInteraction(SettingsUserInteraction.ChangeDistanceDimension(distanceDimension = distanceDimension))
    }
    Column {
        radioOptions.forEach { distanceDimension ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        role = Role.RadioButton,
                        selected = (distanceDimension == selectedOption),
                        onClick = {
                            onPickMode(distanceDimension)
                        }
                    )
                    .padding(horizontal = 16.dp)
            ) {
                AppRadioButton(isSelected = distanceDimension == selectedOption) {
                    onPickMode(distanceDimension)
                }
                MediumText(text = distanceDimension.dimensionName)
            }
        }
    }
}
