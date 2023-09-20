package com.desuzed.everyweather.presentation.features.location_main.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.desuzed.everyweather.R
import com.desuzed.everyweather.data.room.FavoriteLocationDto
import com.desuzed.everyweather.presentation.features.location_main.LocationUserInteraction
import com.desuzed.everyweather.ui.elements.AppDialog
import com.desuzed.everyweather.ui.elements.MediumText
import com.desuzed.everyweather.ui.elements.OutlinedIconEditText
import com.desuzed.everyweather.ui.elements.RegularText
import com.desuzed.everyweather.ui.theming.EveryweatherTheme

@Composable
fun EditLocationDialogContent(
    editLocationText: String,
    onNewEditLocationText: (text: String) -> Unit,
    location: FavoriteLocationDto,
    onUserInteraction: (LocationUserInteraction) -> Unit,
) {
    val onDismiss = {
        onUserInteraction(LocationUserInteraction.ToggleEditFavoriteLocationDialog(null))
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
            MediumText(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                maxLines = Int.MAX_VALUE,
                text = stringResource(id = R.string.change_location_name),
            )
            OutlinedIconEditText(
                text = editLocationText,
                modifier = Modifier
                    .padding(vertical = dimensionResource(id = R.dimen.dimen_20)),
                hint = stringResource(id = R.string.enter_name),
                onTextChanged = onNewEditLocationText,
                backgroundColor = EveryweatherTheme.colors.editTextBg,
                isLoading = false,
                iconResId = R.drawable.ic_round_save_24,
            ) {
                onUserInteraction(
                    LocationUserInteraction.UpdateFavoriteLocation(
                        favoriteLocationDto = location,
                    )
                )
            }
            RegularText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = R.dimen.dimen_10)),
                textAlign = TextAlign.End,
                color = EveryweatherTheme.colors.urlLinkTextColor,
                text = stringResource(id = R.string.set_default_name),
                onClick = {
                    onUserInteraction(LocationUserInteraction.SetDefaultLocationName(location))
                }
            )
        }
    }
}