package com.desuzed.everyweather.presentation.features.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.desuzed.everyweather.R
import com.desuzed.everyweather.presentation.ui.settings.BaseSettingItem
import com.desuzed.everyweather.presentation.ui.settings.SettingUiItem
import com.desuzed.everyweather.ui.elements.AppRadioButton
import com.desuzed.everyweather.ui.elements.MediumText

@Composable
fun <S : Enum<*>, I : BaseSettingItem> SettingPickerContent(
    selectedItem: I,
    radioOptions: List<SettingUiItem<S>>,
    onPicked: (S) -> Unit,
) {
    Column(modifier = Modifier.padding(10.dp)) {
        radioOptions.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        role = Role.RadioButton,
                        selected = item.setting.name == selectedItem.id,
                        onClick = {
                            onPicked(item.setting)
                        }
                    )
                    .padding(vertical = dimensionResource(id = R.dimen.dimen_4)),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppRadioButton(
                    isSelected = item.setting.name == selectedItem.id,
                ) {
                    onPicked(item.setting)
                }
                MediumText(text = stringResource(id = item.textId))
            }
        }
    }
}