package com.desuzed.everyweather.presentation.features.location_main.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.location.geo.GeoResponse
import com.desuzed.everyweather.presentation.features.location_main.LocationUserInteraction
import com.desuzed.everyweather.ui.elements.BoldText
import com.desuzed.everyweather.ui.elements.LinkText
import com.desuzed.everyweather.ui.elements.MediumText
import com.desuzed.everyweather.ui.theming.EveryweatherTheme

@Composable
fun GeoLocationsPickerContent(
    geoList: List<GeoResponse>,
    onUserInteraction: (LocationUserInteraction) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column {
        val inputText = stringResource(id = R.string.search_by)
        val startIndex = inputText.indexOf("L")
        LinkText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = dimensionResource(id = R.dimen.dimen_4),
                    horizontal = dimensionResource(id = R.dimen.dimen_20)
                ),
            inputText = inputText,
            startIndex = startIndex,
            endIndex = startIndex + 14,
            url = stringResource(id = R.string.location_search_url),
            style = EveryweatherTheme.typography.textSmall.copy(
                color = EveryweatherTheme.colors.textColorPrimary,
                textAlign = TextAlign.Center
            ),
            spannableStringColor = EveryweatherTheme.colors.urlLinkTextColor,
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(interactionSource = interactionSource, indication = null) {
                            onUserInteraction(LocationUserInteraction.ConfirmFoundLocation(geoItem))
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MediumText(
                        modifier = Modifier.padding(
                            vertical = dimensionResource(id = R.dimen.dimen_10),
                            horizontal = dimensionResource(id = R.dimen.dimen_20),
                        ),
                        text = geoItem.name, maxLines = 6
                    )
                }
                Divider(color = EveryweatherTheme.colors.editTextStrokeColor.copy(alpha = 0.3f))
            }
        }
    }
}