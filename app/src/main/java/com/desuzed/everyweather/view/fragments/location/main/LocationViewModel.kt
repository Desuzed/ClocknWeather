package com.desuzed.everyweather.view.fragments.location.main

import androidx.lifecycle.viewModelScope
import com.desuzed.everyweather.data.network.ActionResultProvider
import com.desuzed.everyweather.data.repository.RepositoryApp
import com.desuzed.everyweather.data.room.FavoriteLocationDto
import com.desuzed.everyweather.view.BaseViewModel
import com.desuzed.everyweather.view.fragments.weather.main.WeatherMainFragment
import kotlinx.coroutines.launch

class LocationViewModel(private val repo: RepositoryApp) :
    BaseViewModel<LocationMainState, LocationMainAction>(LocationMainState()) {

    init {
        collect(repo.getAllLocations(), ::onNewLocations)
    }

    fun onNewGeoText(text: String) {
        setState { copy(geoText = text) }
    }

    fun loadCachedLocation() = repo.loadForecastFromCache()?.location

    /**
     * Deletes item from DB. If success or not, user gets notification
     */
    fun deleteFavoriteLocation(favoriteLocationDto: FavoriteLocationDto) = viewModelScope.launch {
        val deleted = repo.deleteItem(favoriteLocationDto)
        if (deleted) onSuccess(ActionResultProvider.DELETED)
        else onError(ActionResultProvider.FAIL)
    }

    fun onError(code: Int) {
        val message = repo.parseCode(code)
        setAction(LocationMainAction.ShowToast(message))
    }

    fun findTypedLocation() {
        setAction(
            LocationMainAction.NavigateToWeather(
                state.value.geoText,
                WeatherMainFragment.QUERY_KEY
            )
        )
        setState { copy(geoText = "") }
    }

    private fun onSuccess(code: Int) {
        val message = repo.parseCode(code)
        setAction(LocationMainAction.ShowToast(message))
    }

    private fun onNewLocations(locationsList: List<FavoriteLocationDto>) {
        setState { copy(locations = locationsList) }
    }
}