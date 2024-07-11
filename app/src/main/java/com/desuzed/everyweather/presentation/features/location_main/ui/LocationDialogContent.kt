package com.desuzed.everyweather.presentation.features.location_main.ui

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.location.geo.GeoData
import com.desuzed.everyweather.presentation.features.location_main.LocationDialog
import com.desuzed.everyweather.presentation.features.location_main.LocationUserInteraction
import com.desuzed.everyweather.ui.elements.AppAlertDialog
import com.desuzed.everyweather.ui.elements.AppDialog

@Composable
fun LocationDialogContent(
    dialog: LocationDialog?,
    geoData: List<GeoData>?,
    editLocationText: String,
    onUserInteraction: (LocationUserInteraction) -> Unit,
) {
    dialog?.let {
        when (dialog) {
            LocationDialog.ConfirmPickedLocation -> {
                AppAlertDialog(
                    title = stringResource(id = R.string.load_weather_of_this_location),
                    onPositiveButtonClick = {
                        onUserInteraction(LocationUserInteraction.NewLocationConfirm)
                    },
                    onDismiss = {
                        onUserInteraction(LocationUserInteraction.DismissConfirmPinDialog)
                    },
                )
            }

            is LocationDialog.EditLocation -> {
                EditLocationDialogContent(
                    editLocationText = editLocationText,
                    onNewEditLocationText = {
                        onUserInteraction(LocationUserInteraction.EditLocationText(it))
                    },
                    location = dialog.favoriteLocation,
                    onUserInteraction = onUserInteraction,
                )
            }

            LocationDialog.GeoPickerData -> {
                geoData?.let {
                    AppDialog(
                        modifier = Modifier.fillMaxHeight(fraction = 0.9f),
                        onDismiss = {
                            onUserInteraction(LocationUserInteraction.DismissDialog)
                        }
                    ) {
                        GeoLocationsPickerContent(geoData, onUserInteraction)
                    }
                }
            }

            LocationDialog.RequireLocationPermissions -> {
                RequirePermissionsDialogContent(onUserInteraction)
            }

            is LocationDialog.DeleteLocation -> {
                AppAlertDialog(title = stringResource(id = R.string.delete),
                    onPositiveButtonClick = {
                        onUserInteraction(
                            LocationUserInteraction.DeleteFavoriteLocation(dialog.favoriteLocation)
                        )
                    }, onDismiss = {
                        onUserInteraction(LocationUserInteraction.DismissDialog)
                    }
                )
            }
        }
    }
}