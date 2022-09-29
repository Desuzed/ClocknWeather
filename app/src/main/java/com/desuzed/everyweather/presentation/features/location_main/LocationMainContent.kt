package com.desuzed.everyweather.presentation.features.location_main

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.desuzed.everyweather.MockWeatherObject
import com.desuzed.everyweather.R
import com.desuzed.everyweather.data.room.FavoriteLocationDto
import com.desuzed.everyweather.domain.model.location.geo.GeoResponse
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
            LocationMainPageContent(
                state = state,
                onUserInteraction = onUserInteraction,
                onGeoTextChanged = onGeoTextChanged,
                showDeleteDialog = showDeleteDialog,
            )
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
            if (state.showPickerDialog && state.geoResponses != null) {
                AppDialog(
                    modifier = Modifier.fillMaxHeight(fraction = 0.9f),
                    onDismiss = {
                        onUserInteraction(LocationUserInteraction.DismissLocationPicker)
                    }) {
                    GeoLocationsPickerContent(state.geoResponses, onUserInteraction)
                }
            }
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
            if (state.showRequireLocationPermissionsDialog) {
                RequirePermissionsDialogContent(onUserInteraction)
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

@Composable
fun RequirePermissionsDialogContent(
    onUserInteraction: (LocationUserInteraction) -> Unit,
) {
    val onDismiss = {
        onUserInteraction(LocationUserInteraction.DismissLocationPermissionsDialog)
    }
    AppDialog(
        modifier = Modifier,
        onDismiss = onDismiss
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.dimen_20))
                .fillMaxWidth()
        ) {
            BoldText(
                text = stringResource(id = R.string.require_location_permissions_title),
                textAlign = TextAlign.Center
            )
            RegularText(
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.dimen_10)),
                text = stringResource(id = R.string.require_location_permissions_description),
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = R.dimen.dimen_20)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RegularText(
                    text = stringResource(id = R.string.cancel),
                    onClick = onDismiss
                )
                RoundedButton(
                    text = stringResource(id = R.string.allow),
                    onClick = { onUserInteraction(LocationUserInteraction.RequestLocationPermissions) }
                )
            }
        }
    }
}

@Composable
fun LocationMainPageContent(
    state: LocationMainState,
    onUserInteraction: (LocationUserInteraction) -> Unit,
    onGeoTextChanged: (text: String) -> Unit,
    showDeleteDialog: MutableState<FavoriteLocationDto?>
) {
    Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.dimen_10))) {
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
            OutlinedEditText(
                text = state.geoText,
                modifier = Modifier
                    .padding(
                        top = dimensionResource(id = R.dimen.dimen_10),
                        bottom = dimensionResource(id = R.dimen.dimen_10),
                    )
                    .weight(1f),
                hint = stringResource(id = R.string.search_hint),
                onTextChanged = onGeoTextChanged,
                onSearchClick = { onUserInteraction(LocationUserInteraction.FindByQuery) },
                isLoading = state.isLoading
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
}

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