package com.desuzed.everyweather.presentation.features.location_main

import com.desuzed.everyweather.domain.model.location.FavoriteLocation
import com.desuzed.everyweather.domain.model.location.UserLatLng
import com.desuzed.everyweather.domain.model.location.geo.GeoData
import com.desuzed.everyweather.presentation.base.UserInteraction

sealed interface LocationUserInteraction : UserInteraction {
    data object MyLocation : LocationUserInteraction
    data object DismissDialog : LocationUserInteraction
    data object Settings : LocationUserInteraction
    class ToggleMap(val isVisible: Boolean) : LocationUserInteraction
    class EditLocationText(val input: String) : LocationUserInteraction
    class GeoInputQuery(val input: String) : LocationUserInteraction
    data object FindByQuery : LocationUserInteraction
    data object OnBackClick : LocationUserInteraction
    data object RequestLocationPermissions : LocationUserInteraction
    data object Redirection : LocationUserInteraction
    data object NewLocationConfirm : LocationUserInteraction
    data object DismissConfirmPinDialog : LocationUserInteraction

    //todo delete by id
    class ShowDeleteFavoriteLocation(val item: FavoriteLocation) : LocationUserInteraction
    class NewLocationPicked(val location: UserLatLng) : LocationUserInteraction
    class FavoriteLocationClick(val favoriteLocationDto: FavoriteLocation) : LocationUserInteraction
    class ConfirmFoundLocation(val geo: GeoData) : LocationUserInteraction
    class NavigateToWeather(val latLng: UserLatLng) : LocationUserInteraction
    class ToggleEditFavoriteLocationDialog(val item: FavoriteLocation) : LocationUserInteraction
    class SetDefaultLocationName(val item: FavoriteLocation) : LocationUserInteraction
    class DeleteFavoriteLocation(
        val favoriteLocationDto: FavoriteLocation
    ) : LocationUserInteraction

    class UpdateFavoriteLocation(val favoriteLocationDto: FavoriteLocation) :
        LocationUserInteraction
}