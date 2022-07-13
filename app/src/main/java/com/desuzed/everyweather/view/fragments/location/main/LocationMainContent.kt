package com.desuzed.everyweather.view.fragments.location.main

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.desuzed.everyweather.MockWeatherObject
import com.desuzed.everyweather.R
import com.desuzed.everyweather.data.room.FavoriteLocationDto
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
    LocationMainContent(
        state = LocationMainState(locations = MockWeatherObject.locations),
        onUserInteraction = {},
        onGeoTextChanged = {},
    )
}

@Composable
fun LocationMainContent(
    state: LocationMainState,
    onUserInteraction: (LocationUserInteraction) -> Unit,
    onGeoTextChanged: (text: String) -> Unit,
) {
    EveryweatherTheme {
        val showDeleteDialog = remember { mutableStateOf<FavoriteLocationDto?>(null) }
        GradientBox(
            colors = listOf(
                EveryweatherTheme.colors.secondaryGradientStart,
                EveryweatherTheme.colors.secondaryGradientEnd,
            )
        ) {
            Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.dimen_10))) {
                OutlinedEditText(
                    text = state.geoText,
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.dimen_10)),
                    hint = stringResource(id = R.string.search_hint),
                    onTextChanged = onGeoTextChanged,
                    onSearchClick = { onUserInteraction(LocationUserInteraction.FindByQuery) },
                )
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
                            onLongClick = { showDeleteDialog.value = it }
                        )
                    }
                }
                RoundedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(id = R.dimen.dimen_10)),
                    onClick = { onUserInteraction(LocationUserInteraction.FindOnMap) },
                    text = stringResource(id = R.string.find_on_map)
                )
            }
            FloatingButton(
                id = R.drawable.ic_my_location,
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(id = R.dimen.dimen_20),
                        vertical = dimensionResource(id = R.dimen.dimen_100)
                    )
                    .align(Alignment.BottomEnd),
                onClick = { onUserInteraction(LocationUserInteraction.MyLocation) },
            )
            if (showDeleteDialog.value != null) {
                AppAlertDialog(title = stringResource(id = R.string.delete),
                    onPositiveButtonClick = {
                        onUserInteraction(
                            LocationUserInteraction.DeleteFavoriteLocation(
                                showDeleteDialog.value!!
                            )
                        )
                        showDeleteDialog.value = null
                    }, onDismiss = {
                        showDeleteDialog.value = null
                    }
                )
            }
            if (state.locations.isEmpty()) {
                RegularText(
                    text = stringResource(id = R.string.your_saved_cities_list_is_empty),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.dimen_20))
                )
            }
        }
    }
}