package com.desuzed.everyweather.presentation.features.locatation_map

import androidx.lifecycle.viewModelScope
import com.desuzed.everyweather.data.repository.RepositoryApp
import com.desuzed.everyweather.domain.model.UserLatLng
import com.desuzed.everyweather.presentation.base.BaseViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MapLocationViewModel(private val repo: RepositoryApp) :
    BaseViewModel<MapState, MapAction>(MapState()) {

    fun initState() {
        setState { copy(location = repo.loadForecastFromCache()?.location) }
    }

    fun onNewLocationPicked(newLocation: LatLng) {
        setState { copy(shouldShowDialog = true, newPickedLocation = newLocation) }
    }

    fun onDismiss() {
        setState { copy(shouldShowDialog = false) }
    }

    //todo refactoring userLatLng
    fun onNewLocationConfirm() {
        viewModelScope.launch {
            setState { copy(shouldShowDialog = false, loadNewLocationWeather = true) }
            delay(1000)
            val latLng = state.value.newPickedLocation
            if (latLng != null) {
                val userLatLng = UserLatLng(
                    latLng.latitude.toFloat(),
                    latLng.longitude.toFloat(),
                    System.currentTimeMillis()
                )
                setAction(MapAction.NavigateToWeather(userLatLng.toString()))
            }
            setState { copy(newPickedLocation = null, loadNewLocationWeather = false) }
        }
    }

}