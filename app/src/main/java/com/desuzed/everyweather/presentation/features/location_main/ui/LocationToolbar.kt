package com.desuzed.everyweather.presentation.features.location_main.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.desuzed.everyweather.R
import com.desuzed.everyweather.presentation.features.location_main.LocationUserInteraction
import com.desuzed.everyweather.ui.AppPreview
import com.desuzed.everyweather.ui.elements.OutlinedIconEditText
import com.desuzed.everyweather.ui.theming.EveryweatherTheme
import com.desuzed.everyweather.util.Constants

@AppPreview
@Composable
private fun Preview() {
    EveryweatherTheme {
        LocationToolbar(
            isLoading = false,
            geoText = Constants.EMPTY_STRING,
            onGeoTextChanged = {},
            onUserInteraction = {},
        )
    }
}

@Composable
fun LocationToolbar(
    isLoading: Boolean,
    geoText: String,
    onGeoTextChanged: (text: String) -> Unit,
    onUserInteraction: (LocationUserInteraction) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(
            modifier = Modifier.size(dimensionResource(id = R.dimen.dimen_33)),
            onClick = { onUserInteraction(LocationUserInteraction.OnBackClick) },
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_round_arrow_back),
                    contentDescription = Constants.EMPTY_STRING,
                    tint = EveryweatherTheme.colors.textColorPrimary
                )
            }
        )
        OutlinedIconEditText(
            text = geoText,
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
            isLoading = isLoading,
            iconResId = R.drawable.ic_round_search,
        )
        IconButton(
            modifier = Modifier.size(dimensionResource(id = R.dimen.dimen_50)),
            onClick = { onUserInteraction(LocationUserInteraction.Settings) },
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_settings),
                    contentDescription = Constants.EMPTY_STRING,
                    tint = EveryweatherTheme.colors.secondary
                )
            }
        )
    }
}