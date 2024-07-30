package com.desuzed.everyweather.presentation.features.settings.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.app_update.InAppUpdateStatus
import com.desuzed.everyweather.presentation.features.settings.SettingsAction
import com.desuzed.everyweather.ui.AppPreview
import com.desuzed.everyweather.ui.elements.MediumText
import com.desuzed.everyweather.ui.elements.RoundedButton
import com.desuzed.everyweather.ui.theming.EveryweatherTheme

@AppPreview
@Composable
private fun Preview() {
    EveryweatherTheme {
        SettingsAppUpdateContent(
            updateStatus = InAppUpdateStatus.READY_TO_INSTALL,
            onAction = {},
        )
    }
}

@Composable
fun SettingsAppUpdateContent(
    updateStatus: InAppUpdateStatus?,
    onAction: (SettingsAction) -> Unit,
) {
    if (updateStatus != null) {
        val inAppUpdateUiParams = SettingsInAppUpdateUiParams.fromInAppUpdateStatus(updateStatus)
        MediumText(
            text = stringResource(id = inAppUpdateUiParams.titleTextId),
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
                onAction(inAppUpdateUiParams.action)
            },
            text = stringResource(id = inAppUpdateUiParams.buttonTextId),
        )
    }
}