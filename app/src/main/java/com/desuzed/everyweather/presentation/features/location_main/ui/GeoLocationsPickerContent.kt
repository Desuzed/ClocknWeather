package com.desuzed.everyweather.presentation.features.location_main.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.location.geo.GeoData
import com.desuzed.everyweather.presentation.features.location_main.LocationUserInteraction
import com.desuzed.everyweather.ui.AppPreview
import com.desuzed.everyweather.ui.elements.BoldText
import com.desuzed.everyweather.ui.elements.LinkText
import com.desuzed.everyweather.ui.theming.EveryweatherTheme

@AppPreview
@Composable
private fun Preview() {
    EveryweatherTheme {
        GeoLocationsPickerContent(
            geoList = listOf(
                GeoData(
                    lat = "11",
                    lon = "22",
                    name = "name",
                    importance = 0f,
                )
            ),
            onUserInteraction = {},
        )
    }
}

private const val GEO_DATA_START_LETTER = "L"
private const val GEO_DATA_LINK_LENGTH = 14

@Composable
fun GeoLocationsPickerContent(
    geoList: List<GeoData>,
    onUserInteraction: (LocationUserInteraction) -> Unit,
) {
    Column {
        val inputText = stringResource(id = R.string.search_by)
        val startIndex = inputText.indexOf(GEO_DATA_START_LETTER)
        LinkText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = dimensionResource(id = R.dimen.dimen_4),
                    horizontal = dimensionResource(id = R.dimen.dimen_20)
                ),
            inputText = inputText,
            startIndex = startIndex,
            endIndex = startIndex + GEO_DATA_LINK_LENGTH,
            url = stringResource(id = R.string.location_search_url),
            style = EveryweatherTheme.typography.textSmall.copy(
                color = EveryweatherTheme.colors.onBackgroundPrimary,
                textAlign = TextAlign.Center,
            ),
            spannableStringColor = EveryweatherTheme.colors.accent,
            onClick = { onUserInteraction(LocationUserInteraction.Redirection) }
        )
        BoldText(
            text = stringResource(id = R.string.results_were_found, geoList.size),
            modifier = Modifier
                .padding(horizontal = dimensionResource(id = R.dimen.dimen_20))
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        LazyColumn(modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.dimen_8))) {
            items(items = geoList) { geoItem ->
                GeoDataItem(geoData = geoItem, onUserInteraction)
            }
        }
    }
}