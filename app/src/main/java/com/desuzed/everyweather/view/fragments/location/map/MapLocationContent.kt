package com.desuzed.everyweather.view.fragments.location.map

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.desuzed.everyweather.R
import com.desuzed.everyweather.ui.elements.AppAlertDialog
import com.desuzed.everyweather.ui.elements.RegularText
import com.desuzed.everyweather.ui.theming.EveryweatherTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapLocationContent(
    state: MapState,
    onNewLocation: (LatLng) -> Unit,
    onNewLocationConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    EveryweatherTheme {
        Surface(
            modifier = Modifier.height(dimensionResource(id = R.dimen.dimen_350)),
            shape = RoundedCornerShape(
                topStart = dimensionResource(id = R.dimen.corner_radius_30),
                topEnd = dimensionResource(id = R.dimen.corner_radius_30)
            ),
            color = EveryweatherTheme.colors.bottomDialogBackground
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = dimensionResource(id = R.dimen.dimen_20),
                        start = dimensionResource(id = R.dimen.dimen_10),
                        end = dimensionResource(id = R.dimen.dimen_10),
                        bottom = dimensionResource(id = R.dimen.dimen_10),
                    )
            ) {
                val oldMarker = if (state.location != null) {
                    LatLng(state.location.lat.toDouble(), state.location.lon.toDouble())
                } else null
                val cameraPositionState = rememberCameraPositionState {
                    if (oldMarker != null)
                        position = CameraPosition.fromLatLngZoom(oldMarker, 11f)
                }

                Column {
                    RegularText(
                        text = stringResource(id = R.string.set_place_on_map),
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    GoogleMap(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = dimensionResource(id = R.dimen.dimen_10)),
                        cameraPositionState = cameraPositionState,
                        onMapClick = onNewLocation
                    ) {
                        val showNewMarker =
                            state.newPickedLocation != null && state.loadNewLocationWeather
                        if (oldMarker != null)
                            Marker(
                                position = oldMarker,
                                title = state.location?.name ?: "",
                                visible = !showNewMarker,
                            )
                        if (showNewMarker)
                            Marker(
                                position = state.newPickedLocation!!,
                            )
                    }
                    if (state.shouldShowDialog)
                        AppAlertDialog(
                            title = stringResource(id = R.string.load_weather_of_this_location),
                            onPositiveButtonClick = onNewLocationConfirm,
                            onDismiss = onDismiss,
                        )
                }
            }
        }
    }
}