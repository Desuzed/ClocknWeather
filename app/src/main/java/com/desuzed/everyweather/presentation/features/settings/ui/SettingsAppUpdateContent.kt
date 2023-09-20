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
import com.desuzed.everyweather.presentation.features.settings.SettingsUserInteraction
import com.desuzed.everyweather.ui.elements.MediumText
import com.desuzed.everyweather.ui.elements.RoundedButton

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