package com.desuzed.everyweather.presentation.features.locatation_map

import androidx.lifecycle.viewModelScope
import com.desuzed.everyweather.analytics.MapLocationAnalytics
import com.desuzed.everyweather.domain.model.UserLatLng
import com.desuzed.everyweather.domain.repository.local.SharedPrefsProvider
import com.desuzed.everyweather.presentation.base.BaseViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MapLocationViewModel(
    private val sharedPrefsProvider: SharedPrefsProvider,
    private val analytics: MapLocationAnalytics
) :
    BaseViewModel<MapState, MapAction>(MapState()) {

    fun onUserInteraction(interaction: MapUserInteraction) {
        analytics.onUserInteraction(interaction)
        when (interaction) {
            is MapUserInteraction.NewLocationPicked -> onNewLocationPicked(interaction.location)
            MapUserInteraction.DismissDialog -> onDismiss()
            MapUserInteraction.NewLocationConfirm -> onNewLocationConfirm()
        }
    }

    fun initState() {
        setState { copy(location = sharedPrefsProvider.loadForecastFromCache()?.location) }
    }

    private fun onNewLocationPicked(newLocation: LatLng) {
        setState { copy(shouldShowDialog = true, newPickedLocation = newLocation) }
    }

    private fun onDismiss() {
        setState { copy(shouldShowDialog = false) }
    }

    //todo refactoring userLatLng
    private fun onNewLocationConfirm() {
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