package com.desuzed.everyweather.presentation.features.location_main

import com.desuzed.everyweather.domain.model.location.FavoriteLocation
import com.desuzed.everyweather.domain.model.location.UserLatLng
import com.desuzed.everyweather.domain.model.location.geo.GeoData
import com.desuzed.everyweather.presentation.base.Action

sealed interface LocationAction : Action {
    data object MyLocation : LocationAction
    data object DismissDialog : LocationAction
    data object Settings : LocationAction
    class ToggleMap(val isVisible: Boolean) : LocationAction
    class EditLocationText(val input: String) : LocationAction
    class GeoInputQuery(val input: String) : LocationAction
    data object FindByQuery : LocationAction
    data object OnBackClick : LocationAction
    data object RequestLocationPermissions : LocationAction
    data object Redirection : LocationAction
    data object NewLocationConfirm : LocationAction
    data object DismissConfirmPinDialog : LocationAction

    //todo delete by id
    class ShowDeleteFavoriteLocation(val item: FavoriteLocation) : LocationAction
    class NewLocationPicked(val location: UserLatLng) : LocationAction
    class FavoriteLocationClick(val favoriteLocationDto: FavoriteLocation) : LocationAction
    class ConfirmFoundLocation(val geo: GeoData) : LocationAction
    class NavigateToWeather(val latLng: UserLatLng) : LocationAction
    class ToggleEditFavoriteLocationDialog(val item: FavoriteLocation) : LocationAction
    class SetDefaultLocationName(val item: FavoriteLocation) : LocationAction
    class DeleteFavoriteLocation(
        val favoriteLocationDto: FavoriteLocation
    ) : LocationAction

    class UpdateFavoriteLocation(val favoriteLocationDto: FavoriteLocation) :
        LocationAction
}