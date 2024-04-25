package com.desuzed.everyweather.presentation.features.location_main

import com.desuzed.everyweather.domain.model.location.FavoriteLocation
import com.desuzed.everyweather.domain.model.location.UserLatLng
import com.desuzed.everyweather.domain.model.location.geo.GeoData
import com.desuzed.everyweather.presentation.base.UserInteraction

sealed interface LocationUserInteraction : UserInteraction {
    object MyLocation : LocationUserInteraction
    object Settings : LocationUserInteraction
    object FindOnMap : LocationUserInteraction
    object FindByQuery : LocationUserInteraction
    object OnBackClick : LocationUserInteraction
    object RequestLocationPermissions : LocationUserInteraction
    object DismissLocationPicker : LocationUserInteraction
    object DismissLocationPermissionsDialog : LocationUserInteraction
    object Redirection : LocationUserInteraction
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