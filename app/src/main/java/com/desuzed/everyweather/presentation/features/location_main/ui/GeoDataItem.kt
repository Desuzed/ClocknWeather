package com.desuzed.everyweather.presentation.features.location_main.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.location.geo.GeoData
import com.desuzed.everyweather.presentation.features.location_main.LocationUserInteraction
import com.desuzed.everyweather.ui.AppPreview
import com.desuzed.everyweather.ui.elements.MediumText
import com.desuzed.everyweather.ui.theming.EveryweatherTheme

@AppPreview
@Composable
private fun Preview() {
    EveryweatherTheme {
        GeoDataItem(
            geoData = GeoData(
                lat = "11",
                lon = "22",
                name = "name",
                importance = 0f,
            ),
            onUserInteraction = {},
        )
    }
}

@Composable
fun GeoDataItem(
    geoData: GeoData,
    onUserInteraction: (LocationUserInteraction) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(interactionSource = interactionSource, indication = null) {
                onUserInteraction(LocationUserInteraction.ConfirmFoundLocation(geoData))
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        MediumText(
            modifier = Modifier.padding(
                vertical = dimensionResource(id = R.dimen.dimen_10),
                horizontal = dimensionResource(id = R.dimen.dimen_20),
            ),
            text = geoData.name,
            maxLines = 6,
        )
    }
    Divider(color = EveryweatherTheme.colors.editTextStrokeColor.copy(alpha = 0.3f))
}
