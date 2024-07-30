package com.desuzed.everyweather.presentation.features.location_main

import com.desuzed.everyweather.analytics.LocationMainAnalytics
import com.desuzed.everyweather.data.repository.providers.UserLocationProvider
import com.desuzed.everyweather.data.repository.providers.action_result.GeoActionResultProvider
import com.desuzed.everyweather.domain.interactor.LocationInteractor
import com.desuzed.everyweather.domain.model.location.FavoriteLocation
import com.desuzed.everyweather.domain.model.location.UserLatLng
import com.desuzed.everyweather.domain.model.location.geo.GeoData
import com.desuzed.everyweather.domain.model.result.QueryResult
import com.desuzed.everyweather.domain.repository.local.SharedPrefsProvider
import com.desuzed.everyweather.domain.repository.provider.ActionResultProvider
import com.desuzed.everyweather.presentation.base.BaseViewModel
import com.desuzed.everyweather.util.Constants.EMPTY_STRING
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow

class LocationViewModel(
    private val locationInteractor: LocationInteractor,
    private val userLocationProvider: UserLocationProvider,
    private val analytics: LocationMainAnalytics,
    private val sharedPrefsProvider: SharedPrefsProvider,
) : BaseViewModel<LocationMainState, LocationMainEffect, LocationAction>(LocationMainState()) {

    private val queryResultFlow = MutableSharedFlow<QueryResult>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        collect(locationInteractor.getAllLocations(), ::onNewLocations)
        collect(queryResultFlow, ::collectQueryResult)
        initMapPin()
    }

    override fun onAction(action: LocationAction) {
        analytics.onAction(action)
        when (action) {
            is LocationAction.DeleteFavoriteLocation -> deleteFavoriteLocation(
                action.favoriteLocationDto
            )

            is LocationAction.ConfirmFoundLocation -> onConfirmLocation(action.geo)
            is LocationAction.FavoriteLocationClick -> onFavoriteLocation(action.favoriteLocationDto)
            is LocationAction.NavigateToWeather -> navigateToWeatherWithDelay(action.latLng)
            LocationAction.Redirection -> redirectToLocationApiPage()
            LocationAction.FindByQuery -> findTypedLocation()
            is LocationAction.ToggleMap -> toggleMap(action.isVisible)
            LocationAction.MyLocation -> setSideEffect(LocationMainEffect.MyLocation)
            LocationAction.Settings -> setSideEffect(LocationMainEffect.NavigateToSettings)
            LocationAction.OnBackClick -> setSideEffect(LocationMainEffect.NavigateBack)
            LocationAction.RequestLocationPermissions -> onRequestPermissions()
            is LocationAction.UpdateFavoriteLocation -> updateFavoriteLocation(action.favoriteLocationDto)
            is LocationAction.ToggleEditFavoriteLocationDialog -> onToggle(action.item)
            is LocationAction.SetDefaultLocationName -> setDefaultLocationName(action.item)
            LocationAction.DismissConfirmPinDialog -> onDismissConfirmPinDialog()
            LocationAction.NewLocationConfirm -> onNewLocationConfirm()
            is LocationAction.NewLocationPicked -> onNewLocationPicked(action.location)
            LocationAction.DismissDialog -> onDismissDialog()
            is LocationAction.EditLocationText -> onNewEditLocationText(action.input)
            is LocationAction.GeoInputQuery -> onNewGeoText(action.input)
            is LocationAction.ShowDeleteFavoriteLocation -> onShowDeleteFavoriteLocation(
                action.item
            )
        }
    }

    fun areLocationPermissionsGranted(): Boolean = userLocationProvider.arePermissionsGranted()

    fun launchRequireLocationPermissionsDialog() {
        setState { copy(locationDialog = LocationDialog.RequireLocationPermissions) }
    }

    private fun findTypedLocation() {
        launch {
            setState { copy(isLoading = true) }
            val resultGeo = locationInteractor.fetchGeocodingResultOrError(
                query = state.value.geoText,
            )
            setState {
                copy(
                    geoData = resultGeo.geoData,
                    isLoading = false,
                    locationDialog = LocationDialog.GeoPickerData,
                )
            }
            val resultAction = resultGeo.queryResult
            if (resultAction != null) {
                queryResultFlow.emit(resultAction)
            }
        }

    }

    private fun onDismissDialog() {
        setState { copy(locationDialog = null) }
    }

    private fun onToggle(item: FavoriteLocation) {
        setState {
            copy(
                locationDialog = LocationDialog.EditLocation(item),
                editLocationText = item.customName.ifEmpty { item.cityName },
            )
        }
    }

    private fun onRequestPermissions() {
        setSideEffect(LocationMainEffect.RequestLocationPermissions)
        onDismissDialog()
    }

    private fun onConfirmLocation(geoData: GeoData) {
        setSideEffect(
            LocationMainEffect.NavigateToWeather(
                query = "${geoData.lat},${geoData.lon}",
            )
        )
        setState { copy(geoText = EMPTY_STRING, geoData = null) }
    }

    private fun deleteFavoriteLocation(favoriteLocationDto: FavoriteLocation) =
        launch {
            onDismissDialog()
            val deleted = locationInteractor.deleteFavoriteLocation(favoriteLocationDto)
            if (deleted) {
                onSuccess(ActionResultProvider.DELETED)
            } else {
                onError(ActionResultProvider.FAIL)
            }
        }

    private fun updateFavoriteLocation(favoriteLocationDto: FavoriteLocation) {
        launch {
            onDismissDialog()
            val inputText = state.value.editLocationText
            val locationToSave = favoriteLocationDto.copy(customName = inputText)
            val updated = locationInteractor.updateLocation(locationToSave)
            if (updated) {
                onSuccess(ActionResultProvider.UPDATED)
            } else {
                onError(ActionResultProvider.FAIL)
            }
        }
    }

    private fun setDefaultLocationName(favoriteLocationDto: FavoriteLocation) {
        launch {
            onDismissDialog()
            val locationToSave = favoriteLocationDto.copy(customName = favoriteLocationDto.cityName)
            val updated = locationInteractor.updateLocation(locationToSave)
            if (updated) {
                onSuccess(ActionResultProvider.UPDATED)
            } else {
                onError(ActionResultProvider.FAIL)
            }
        }
    }

    private suspend fun onSuccess(code: Int) {
        queryResultFlow.emit(QueryResult(code))
    }

    private suspend fun onError(code: Int) {
        queryResultFlow.emit(QueryResult(code))
    }

    private fun onNewLocations(locationsList: List<FavoriteLocation>) {
        setState { copy(locations = locationsList) }
    }

    private fun collectQueryResult(queryResult: QueryResult) {
        if (shouldIgnoreError(queryResult.code)) {
            setSideEffect(
                LocationMainEffect.NavigateToWeather(
                    query = queryResult.query,
                )
            )
        } else {
            setSideEffect(LocationMainEffect.ShowSnackbar(queryResult))
        }
    }

    private fun navigateToWeatherWithDelay(latLng: UserLatLng) {
        launch {
            delay(200)
            setSideEffect(LocationMainEffect.NavigateToWeatherWithLatLng(latLng))
        }
    }

    private fun onFavoriteLocation(location: FavoriteLocation) {
        setSideEffect(
            LocationMainEffect.NavigateToWeather(
                query = location.toQuery(),
            )
        )
    }

    private fun redirectToLocationApiPage() {
        launch {
            queryResultFlow.emit(QueryResult(code = ActionResultProvider.REDIRECTION))
        }
    }

    private fun shouldIgnoreError(code: Int): Boolean =
        code == GeoActionResultProvider.RATE_LIMIT
                || code == GeoActionResultProvider.ACCESS_RESTRICTED
                || code == GeoActionResultProvider.INVALID_TOKEN

    private fun onNewLocationPicked(newLocation: UserLatLng) {
        setState {
            copy(
                newPickedLocation = newLocation,
                locationDialog = LocationDialog.ConfirmPickedLocation,
            )
        }
    }

    private fun onDismissConfirmPinDialog() {
        setState { copy(newPickedLocation = null) }
        onDismissDialog()
    }

    private fun onShowDeleteFavoriteLocation(item: FavoriteLocation) {
        setState { copy(locationDialog = LocationDialog.DeleteLocation(item)) }
    }

    //todo доработать чтобы маркер перемещался
    private fun onNewLocationConfirm() {
        launch {
            onDismissDialog()
            delay(ONE_SEC)
            val latLng = state.value.newPickedLocation
            if (latLng != null) {
                val userLatLng = latLng.copy(time = System.currentTimeMillis())
                toggleMap(false)
                setSideEffect(LocationMainEffect.NavigateToWeatherWithLatLng(userLatLng))
            }
            setState { copy(newPickedLocation = null, loadNewLocationWeather = false) }
        }
    }

    private fun initMapPin() {
        setState { copy(mapPinLocation = sharedPrefsProvider.loadForecastFromCache()?.location) }
    }

    private fun toggleMap(isShown: Boolean) {
        setSideEffect(LocationMainEffect.ToggleMap(isShown))
    }

    private fun onNewGeoText(text: String) {
        setState { copy(geoText = text) }
    }

    private fun onNewEditLocationText(text: String) {
        setState { copy(editLocationText = text) }
    }

    companion object {
        private const val ONE_SEC = 1000L
    }

}