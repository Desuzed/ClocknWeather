package com.desuzed.everyweather.presentation.features.location_main.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.desuzed.everyweather.R
import com.desuzed.everyweather.data.room.FavoriteLocationDto
import com.desuzed.everyweather.presentation.features.location_main.LocationMainState
import com.desuzed.everyweather.presentation.features.location_main.LocationUserInteraction
import com.desuzed.everyweather.ui.elements.LocationItemContent
import com.desuzed.everyweather.ui.elements.OutlinedIconEditText
import com.desuzed.everyweather.ui.elements.RoundedButton
import com.desuzed.everyweather.ui.theming.EveryweatherTheme

@Composable
fun LocationMainPageContent(
    state: LocationMainState,
    onUserInteraction: (LocationUserInteraction) -> Unit,
    onGeoTextChanged: (text: String) -> Unit,
    showDeleteDialog: MutableState<FavoriteLocationDto?>
) {
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(dimensionResource(id = R.dimen.dimen_10))
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                modifier = Modifier.size(dimensionResource(id = R.dimen.dimen_33)),
                onClick = { onUserInteraction(LocationUserInteraction.OnBackClick) },
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_round_arrow_back),
                        contentDescription = "",
                        tint = EveryweatherTheme.colors.textColorPrimary
                    )
                }
            )
            OutlinedIconEditText(
                text = state.geoText,
                modifier = Modifier
                    .padding(
                        top = dimensionResource(id = R.dimen.dimen_10),
                        bottom = dimensionResource(id = R.dimen.dimen_10),
                    )
                    .weight(1f),
                hint = stringResource(id = R.string.search_hint),
                onTextChanged = onGeoTextChanged,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                onIconClick = { onUserInteraction(LocationUserInteraction.FindByQuery) },
                isLoading = state.isLoading,
                iconResId = R.drawable.ic_round_search,
            )
            IconButton(
                modifier = Modifier.size(dimensionResource(id = R.dimen.dimen_50)),
                onClick = { onUserInteraction(LocationUserInteraction.Settings) },
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_settings),
                        contentDescription = "",
                        tint = EveryweatherTheme.colors.secondary
                    )
                }
            )
        }
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(
                    horizontal = dimensionResource(id = R.dimen.dimen_10),
                    vertical = dimensionResource(id = R.dimen.dimen_10)
                )
        ) {
            items(items = state.locations, key = { it.latLon }) { locationItem ->
                LocationItemContent(
                    item = locationItem,
                    onClick = {
                        onUserInteraction(
                            LocationUserInteraction.FavoriteLocation(
                                locationItem
                            )
                        )
                    },
                    onLongClick = { showDeleteDialog.value = it },
                    onEditClick = {
                        onUserInteraction(
                            LocationUserInteraction.ToggleEditFavoriteLocationDialog(it)
                        )
                    }
                )
            }
        }
        RoundedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.dimen_10))
                .navigationBarsPadding()
                .imePadding(),
            onClick = { onUserInteraction(LocationUserInteraction.FindOnMap) },
            text = stringResource(id = R.string.find_on_map)
        )
    }
}