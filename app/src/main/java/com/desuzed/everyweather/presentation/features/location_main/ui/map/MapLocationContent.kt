package com.desuzed.everyweather.presentation.features.location_main.ui.map

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
import com.desuzed.everyweather.domain.model.location.UserLatLng
import com.desuzed.everyweather.domain.model.weather.Location
import com.desuzed.everyweather.presentation.features.location_main.LocationUserInteraction
import com.desuzed.everyweather.ui.AppPreview
import com.desuzed.everyweather.ui.elements.RegularText
import com.desuzed.everyweather.ui.theming.EveryweatherTheme
import com.desuzed.everyweather.util.Constants.EMPTY_STRING
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@AppPreview
@Composable
private fun Preview() {
    EveryweatherTheme {
        MapLocationContent(
            location = null,
            newPickedLocation = null,
            loadNewLocationWeather = false,
            onUserInteraction = {},
        )
    }
}

private const val INITIAL_ZOOM = 11f

@Composable
fun MapLocationContent(
    location: Location?,
    newPickedLocation: UserLatLng?,
    loadNewLocationWeather: Boolean,
    onUserInteraction: (LocationUserInteraction) -> Unit,
) {
    Surface(
        modifier = Modifier.height(dimensionResource(id = R.dimen.dimen_350)),
        shape = RoundedCornerShape(
            topStart = dimensionResource(id = R.dimen.corner_radius_30),
            topEnd = dimensionResource(id = R.dimen.corner_radius_30)
        ),
        color = EveryweatherTheme.colors.tertiaryBackground
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
            val oldMarker = if (location != null) {
                LatLng(location.lat, location.lon)
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
                        onUserInteraction(
                            LocationUserInteraction.NewLocationPicked(
                                location = UserLatLng(
                                    lat = it.latitude,
                                    lon = it.longitude,
                                    time = System.currentTimeMillis(),
                                )
                            )
                        )
                    }
                ) {
                    val showNewMarker =
                        newPickedLocation != null && loadNewLocationWeather
                    if (oldMarker != null)
                        Marker(
                            state = MarkerState(position = oldMarker),
                            title = location?.name ?: EMPTY_STRING,
                            visible = !showNewMarker,
                        )
                    if (showNewMarker)
                        Marker(
                            state = MarkerState(
                                position = LatLng(
                                    newPickedLocation!!.lat,
                                    newPickedLocation.lon,
                                )
                            ),
                        )
                }
            }
        }
    }
}