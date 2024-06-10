package com.desuzed.everyweather.presentation.features.location_main.ui

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.location.FavoriteLocation
import com.desuzed.everyweather.domain.model.location.geo.GeoData
import com.desuzed.everyweather.presentation.features.location_main.LocationUserInteraction
import com.desuzed.everyweather.ui.AppPreview
import com.desuzed.everyweather.ui.elements.AppAlertDialog
import com.desuzed.everyweather.ui.elements.AppDialog
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
            editLocationText = "",
            geoText = "",
            geoData = emptyList(),
            showPickerDialog = false,
            showEditLocationDialog = null,
            showRequireLocationPermissionsDialog = false,
            onUserInteraction = {},
            onGeoTextChanged = {},
            onNewEditLocationText = {},
        )
    }
}

@Composable
fun LocationMainBody(
    locations: List<FavoriteLocation>,
    isLoading: Boolean,
    editLocationText: String,
    geoText: String,
    geoData: List<GeoData>?,
    showPickerDialog: Boolean,
    showEditLocationDialog: FavoriteLocation?,
    showRequireLocationPermissionsDialog: Boolean,
    onUserInteraction: (LocationUserInteraction) -> Unit,
    onGeoTextChanged: (text: String) -> Unit,
    onNewEditLocationText: (text: String) -> Unit,
) {
    val showDeleteDialog = remember { mutableStateOf<FavoriteLocation?>(null) }
    GradientBox(
        modifier = Modifier.fillMaxSize(),
        colors = EveryweatherTheme.colors.primaryBackground,
    ) {
        LocationMainPageContent(
            locations = locations,
            isLoading = isLoading,
            geoText = geoText,
            onUserInteraction = onUserInteraction,
            onGeoTextChanged = onGeoTextChanged,
            onShowDeleteDialog = {
                showDeleteDialog.value = it
            }
        )

        if (showPickerDialog && geoData != null) {
            AppDialog(
                modifier = Modifier.fillMaxHeight(fraction = 0.9f),
                onDismiss = {
                    onUserInteraction(LocationUserInteraction.DismissLocationPicker)
                }) {
                GeoLocationsPickerContent(geoData, onUserInteraction)
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
        if (showRequireLocationPermissionsDialog) {
            RequirePermissionsDialogContent(onUserInteraction)
        }
        if (showEditLocationDialog != null) {
            EditLocationDialogContent(
                editLocationText = editLocationText,
                onNewEditLocationText = onNewEditLocationText,
                location = showEditLocationDialog,
                onUserInteraction = onUserInteraction,
            )
        }
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
            onClick = { onUserInteraction(LocationUserInteraction.MyLocation) },
        )
    }

}
