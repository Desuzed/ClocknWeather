package com.desuzed.everyweather.presentation.features.in_app_update.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.desuzed.everyweather.domain.model.app_update.InAppUpdateStatus
import com.desuzed.everyweather.presentation.features.in_app_update.InAppUpdateAction
import com.desuzed.everyweather.ui.AppPreview
import com.desuzed.everyweather.ui.elements.RegularText
import com.desuzed.everyweather.ui.elements.RoundedButton
import com.desuzed.everyweather.ui.theming.EveryweatherTheme

@AppPreview
@Composable
private fun Preview() {
    EveryweatherTheme {
        InAppUpdateButtonsRow(
            inAppUpdateUiParams = InAppUpdateUiParams.fromInAppUpdateStatus(
                status = InAppUpdateStatus.READY_TO_INSTALL,
            ),
            onAction = {},
        )
    }
}

@Composable
fun InAppUpdateButtonsRow(
    inAppUpdateUiParams: InAppUpdateUiParams,
    onAction: (InAppUpdateAction) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RegularText(
            onClick = {
                onAction(
                    InAppUpdateAction.Dismiss
                )
            },
            text = stringResource(id = inAppUpdateUiParams.negativeButtonTextId)
        )
        RoundedButton(
            onClick = {
                onAction(inAppUpdateUiParams.positiveButtonInteraction)
            },
            text = stringResource(id = inAppUpdateUiParams.positiveButtonTextId)
        )
    }
}

