package com.desuzed.everyweather.presentation.features.location_main.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.location.FavoriteLocation
import com.desuzed.everyweather.presentation.features.location_main.LocationAction
import com.desuzed.everyweather.ui.AppPreview
import com.desuzed.everyweather.ui.elements.FloatingButton
import com.desuzed.everyweather.ui.elements.GradientBox
import com.desuzed.everyweather.ui.elements.RegularText
import com.desuzed.everyweather.ui.theming.EveryweatherTheme

@AppPreview
@Composable
private fun Preview() {
    EveryweatherTheme {
        LocationMainBody(
            locations = emptyList(),
            isLoading = false,
            geoText = "",
            onAction = {},
        )
    }
}

@Composable
fun LocationMainBody(
    locations: List<FavoriteLocation>,
    isLoading: Boolean,
    geoText: String,
    onAction: (LocationAction) -> Unit,
) {
    GradientBox(
        modifier = Modifier.fillMaxSize(),
        colors = EveryweatherTheme.colors.primaryBackground,
    ) {
        LocationMainPageContent(
            locations = locations,
            isLoading = isLoading,
            geoText = geoText,
            onAction = onAction,
            onGeoTextChanged = {
                onAction(LocationAction.GeoInputQuery(it))
            },
            onShowDeleteDialog = {
                onAction(LocationAction.ShowDeleteFavoriteLocation(it))
            }
        )
        if (locations.isEmpty()) {
            RegularText(
                text = stringResource(id = R.string.your_saved_cities_list_is_empty),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.dimen_20))
            )
        }
        FloatingButton(
            id = R.drawable.ic_my_location,
            modifier = Modifier
                .padding(
                    horizontal = dimensionResource(id = R.dimen.dimen_10),
                    vertical = dimensionResource(id = R.dimen.dimen_100)
                )
                .navigationBarsPadding()
                .imePadding()
                .align(Alignment.BottomEnd),
            onClick = { onAction(LocationAction.MyLocation) },
        )
    }

}
