package com.desuzed.everyweather.presentation.features.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.desuzed.everyweather.R
import com.desuzed.everyweather.presentation.features.settings.SettingsAction
import com.desuzed.everyweather.presentation.ui.settings.BaseSettingItem
import com.desuzed.everyweather.presentation.ui.settings.DarkTheme
import com.desuzed.everyweather.presentation.ui.settings.Language
import com.desuzed.everyweather.ui.AppPreview
import com.desuzed.everyweather.ui.elements.BoldText
import com.desuzed.everyweather.ui.elements.MediumText
import com.desuzed.everyweather.ui.elements.RoundedCardItem
import com.desuzed.everyweather.ui.theming.EveryweatherTheme
import com.desuzed.everyweather.util.Constants.EMPTY_STRING

@AppPreview
@Composable
private fun Preview() {
    EveryweatherTheme {
        SettingsMenuGroupContent(
            items = listOf(
                DarkTheme(
                    id = "OFF",
                    categoryStringId = R.string.dark_mode,
                    valueStringId = R.string.on,
                ),
                Language(
                    id = "RU",
                    categoryStringId = R.string.language,
                    valueStringId = R.string.russian,
                )
            ),
            onAction = {},
        )
    }
}

@Composable
fun SettingsMenuGroupContent(
    items: List<BaseSettingItem>,
    onAction: (SettingsAction) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    RoundedCardItem(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.dimen_10))
    ) {
        Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.dimen_10))) {
            items.forEach { item ->
                val onItemCLick: () -> Unit = {
                    onAction(
                        SettingsAction.ShowSettingDialog(item.type)
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(
                            horizontal = dimensionResource(id = R.dimen.dimen_10),
                            vertical = dimensionResource(id = R.dimen.dimen_8),
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
                            onClick = onItemCLick,
                        )
                        MediumText(text = stringResource(item.valueStringId))
                    }
                    Icon(
                        modifier = Modifier.rotate(90f),
                        painter = painterResource(id = R.drawable.ic_arrow_24),
                        contentDescription = EMPTY_STRING,
                        tint = EveryweatherTheme.colors.onBackgroundPrimary,
                    )
                }
            }
        }
    }
}