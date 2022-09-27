package com.desuzed.everyweather.presentation.features.location_main

import com.desuzed.everyweather.data.room.FavoriteLocationDto
import com.desuzed.everyweather.domain.model.location.geo.GeoResponse

sealed interface LocationUserInteraction {
    object MyLocation : LocationUserInteraction
    object Settings : LocationUserInteraction
    object FindOnMap : LocationUserInteraction
    object FindByQuery : LocationUserInteraction
    object OnBackClick : LocationUserInteraction
    object RequestLocationPermissions : LocationUserInteraction
    object DismissLocationPicker : LocationUserInteraction
    object DismissLocationPermissionsDialog : LocationUserInteraction
    class Redirection(val message: String) : LocationUserInteraction
    class FavoriteLocation(val favoriteLocationDto: FavoriteLocationDto) : LocationUserInteraction
    class ConfirmFoundLocation(val geo: GeoResponse) : LocationUserInteraction
    class DeleteFavoriteLocation(
        val favoriteLocationDto: FavoriteLocationDto
    ) : LocationUserInteraction
}