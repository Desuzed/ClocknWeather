package com.desuzed.everyweather.presentation.features.location_main

import androidx.lifecycle.viewModelScope
import com.desuzed.everyweather.data.room.FavoriteLocationDto
import com.desuzed.everyweather.domain.repository.local.RoomProvider
import com.desuzed.everyweather.presentation.base.BaseViewModel
import com.desuzed.everyweather.presentation.features.weather_main.WeatherMainFragment
import com.desuzed.everyweather.util.ActionResultProvider
import kotlinx.coroutines.launch

class LocationViewModel(
    private val roomProvider: RoomProvider,
    private val actionResultProvider: ActionResultProvider
) :
    BaseViewModel<LocationMainState, LocationMainAction>(LocationMainState()) {

    init {
        collect(roomProvider.getAllLocations(), ::onNewLocations)
    }

    fun onNewGeoText(text: String) {
        setState { copy(geoText = text) }
    }

    fun onUserInteraction(userInteraction: LocationUserInteraction) {
        when (userInteraction) {
            is LocationUserInteraction.DeleteFavoriteLocation -> deleteFavoriteLocation(
                userInteraction.favoriteLocationDto
            )
            is LocationUserInteraction.FavoriteLocation -> setAction(
                LocationMainAction.NavigateToWeather(
                    query = userInteraction.favoriteLocationDto.toQuery(),
                    key = WeatherMainFragment.QUERY_KEY
                )
            )
            LocationUserInteraction.FindByQuery -> findTypedLocation()
            LocationUserInteraction.FindOnMap -> setAction(LocationMainAction.ShowMapFragment)
            LocationUserInteraction.MyLocation -> setAction(LocationMainAction.MyLocation)
            LocationUserInteraction.Settings -> setAction(LocationMainAction.NavigateToSettings)
            LocationUserInteraction.OnBackClick -> setAction(LocationMainAction.NavigateBack)
        }
    }

    private fun findTypedLocation() {
        setAction(
            LocationMainAction.NavigateToWeather(
                query = state.value.geoText,
                key = WeatherMainFragment.QUERY_KEY
            )
        )
        setState { copy(geoText = "") }
    }

    private fun deleteFavoriteLocation(favoriteLocationDto: FavoriteLocationDto) =
        viewModelScope.launch {
            val deleted = roomProvider.deleteItem(favoriteLocationDto)
            if (deleted) onSuccess(ActionResultProvider.DELETED)
            else onError(ActionResultProvider.FAIL)
        }

    private fun onSuccess(code: Int) {
        val message = actionResultProvider.parseCode(code).message
        setAction(LocationMainAction.ShowSnackbar(message))
    }

    private fun onError(code: Int) {
        val message = actionResultProvider.parseCode(code).message
        setAction(LocationMainAction.ShowSnackbar(message))
    }

    private fun onNewLocations(locationsList: List<FavoriteLocationDto>) {
        setState { copy(locations = locationsList) }
    }
}