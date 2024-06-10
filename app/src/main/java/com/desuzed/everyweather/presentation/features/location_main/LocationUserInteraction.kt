package com.desuzed.everyweather.presentation.features.location_main

import com.desuzed.everyweather.domain.model.location.FavoriteLocation
import com.desuzed.everyweather.domain.model.location.UserLatLng
import com.desuzed.everyweather.domain.model.location.geo.GeoData
import com.desuzed.everyweather.presentation.base.UserInteraction

sealed interface LocationUserInteraction : UserInteraction {
    data object MyLocation : LocationUserInteraction
    data object Settings : LocationUserInteraction
    data object FindOnMap : LocationUserInteraction
    data object FindByQuery : LocationUserInteraction
    data object OnBackClick : LocationUserInteraction
    data object RequestLocationPermissions : LocationUserInteraction
    data object DismissLocationPicker : LocationUserInteraction
    data object DismissLocationPermissionsDialog : LocationUserInteraction
    data object Redirection : LocationUserInteraction
    class FavoriteLocationClick(val favoriteLocationDto: FavoriteLocation) : LocationUserInteraction
    class ConfirmFoundLocation(val geo: GeoData) : LocationUserInteraction
    class NavigateToWeather(val latLng: UserLatLng) : LocationUserInteraction
    class ToggleEditFavoriteLocationDialog(val item: FavoriteLocation?) : LocationUserInteraction
    class SetDefaultLocationName(val item: FavoriteLocation) : LocationUserInteraction
    class DeleteFavoriteLocation(
        val favoriteLocationDto: FavoriteLocation
    ) : LocationUserInteraction

    class UpdateFavoriteLocation(val favoriteLocationDto: FavoriteLocation) :
        LocationUserInteraction
}