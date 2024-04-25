package com.desuzed.everyweather.presentation.features.location_main.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.location.FavoriteLocation
import com.desuzed.everyweather.presentation.features.location_main.LocationUserInteraction
import com.desuzed.everyweather.ui.AppPreview
import com.desuzed.everyweather.ui.elements.RoundedButton
import com.desuzed.everyweather.ui.theming.EveryweatherTheme
import com.desuzed.everyweather.util.Constants.EMPTY_STRING
import com.desuzed.everyweather.util.Constants.ONE_FLOAT
import com.desuzed.everyweather.util.MockWeatherObject

@AppPreview
@Composable
private fun Preview() {
    EveryweatherTheme {
        LocationMainPageContent(
            locations = MockWeatherObject.locations,
            isLoading = false,
            geoText = EMPTY_STRING,
            onUserInteraction = {},
            onGeoTextChanged = {},
            onShowDeleteDialog = {},
        )
    }
}

@Composable
fun LocationMainPageContent(
    locations: List<FavoriteLocation>,
    isLoading: Boolean,
    geoText: String,
    onGeoTextChanged: (text: String) -> Unit,
    onUserInteraction: (LocationUserInteraction) -> Unit,
    onShowDeleteDialog: (FavoriteLocation) -> Unit,
) {
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(dimensionResource(id = R.dimen.dimen_10))
    ) {
        LocationToolbar(
            isLoading = isLoading,
            geoText = geoText,
            onGeoTextChanged = onGeoTextChanged,
            onUserInteraction = onUserInteraction,
        )
        LazyColumn(
            modifier = Modifier
                .weight(ONE_FLOAT)
                .padding(
                    horizontal = dimensionResource(id = R.dimen.dimen_10),
                    vertical = dimensionResource(id = R.dimen.dimen_10)
                )
        ) {
            items(items = locations, key = { it.latLon }) { locationItem ->
                LocationItemContent(
                    item = locationItem,
                    onClick = {
                        onUserInteraction(
                            LocationUserInteraction.FavoriteLocationClick(
                                locationItem
                            )
                        )
                    },
                    onLongClick = { onShowDeleteDialog(it) },
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