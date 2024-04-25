package com.desuzed.everyweather.presentation.features.locatation_map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.desuzed.everyweather.R
import com.desuzed.everyweather.ui.AppPreview
import com.desuzed.everyweather.ui.elements.AppAlertDialog
import com.desuzed.everyweather.ui.elements.RegularText
import com.desuzed.everyweather.ui.theming.EveryweatherTheme
import com.desuzed.everyweather.util.Constants.EMPTY_STRING
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState

@AppPreview
@Composable
private fun Preview() {
    EveryweatherTheme {
        MapLocationContent(state = MapState(), onUserInteraction = {})
    }
}

private const val INITIAL_ZOOM = 11f

@Composable
fun MapLocationContent(
    state: MapState,
    onUserInteraction: (MapUserInteraction) -> Unit,
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
                    LatLng(state.location.lat, state.location.lon)
                } else null
                val cameraPositionState = rememberCameraPositionState {
                    if (oldMarker != null) {
                        position = CameraPosition.fromLatLngZoom(oldMarker, INITIAL_ZOOM)
                    }
                }

                Column {
                    RegularText(
                        text = stringResource(id = R.string.set_place_on_map),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    GoogleMap(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = dimensionResource(id = R.dimen.dimen_10)),
                        cameraPositionState = cameraPositionState,
                        onMapClick = {
                            onUserInteraction(MapUserInteraction.NewLocationPicked(it))
                        }
                    ) {
                        val showNewMarker =
                            state.newPickedLocation != null && state.loadNewLocationWeather
                        if (oldMarker != null)
                            Marker(
                                position = oldMarker,
                                title = state.location?.name ?: EMPTY_STRING,
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
                            onPositiveButtonClick = {
                                onUserInteraction(MapUserInteraction.NewLocationConfirm)
                            },
                            onDismiss = {
                                onUserInteraction(MapUserInteraction.DismissDialog)
                            },
                        )
                }
            }
        }
    }
}