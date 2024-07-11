package com.desuzed.everyweather.presentation.features.location_main.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.desuzed.everyweather.R
import com.desuzed.everyweather.presentation.features.location_main.LocationUserInteraction
import com.desuzed.everyweather.ui.AppPreview
import com.desuzed.everyweather.ui.elements.AppDialog
import com.desuzed.everyweather.ui.elements.BoldText
import com.desuzed.everyweather.ui.elements.RegularText
import com.desuzed.everyweather.ui.elements.RoundedButton
import com.desuzed.everyweather.ui.theming.EveryweatherTheme

@AppPreview
@Composable
private fun Preview() {
    EveryweatherTheme {
        RequirePermissionsDialogContent(onUserInteraction = {})
    }
}

@Composable
fun RequirePermissionsDialogContent(
    onUserInteraction: (LocationUserInteraction) -> Unit,
) {
    val onDismiss = {
        onUserInteraction(LocationUserInteraction.DismissDialog)
    }
    AppDialog(
        modifier = Modifier,
        onDismiss = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.dimen_20))
                .fillMaxWidth()
        ) {
            BoldText(
                text = stringResource(id = R.string.require_location_permissions_title),
                textAlign = TextAlign.Center,
            )
            RegularText(
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.dimen_10)),
                text = stringResource(id = R.string.require_location_permissions_description),
                textAlign = TextAlign.Center,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = R.dimen.dimen_20)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RegularText(
                    text = stringResource(id = R.string.cancel),
                    onClick = onDismiss,
                )
                RoundedButton(
                    text = stringResource(id = R.string.allow),
                    onClick = {
                        onUserInteraction(LocationUserInteraction.RequestLocationPermissions)
                    },
                )
            }
        }
    }
}