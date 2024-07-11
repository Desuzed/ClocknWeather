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
) : BaseViewModel<LocationMainState, LocationMainAction, LocationUserInteraction>(LocationMainState()) {

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

    override fun onUserInteraction(interaction: LocationUserInteraction) {
        analytics.onUserInteraction(interaction)
        when (interaction) {
            is LocationUserInteraction.DeleteFavoriteLocation -> deleteFavoriteLocation(
                interaction.favoriteLocationDto
            )

            is LocationUserInteraction.ConfirmFoundLocation -> onConfirmLocation(interaction.geo)
            is LocationUserInteraction.FavoriteLocationClick -> onFavoriteLocation(interaction.favoriteLocationDto)
            is LocationUserInteraction.NavigateToWeather -> navigateToWeatherWithDelay(interaction.latLng)
            LocationUserInteraction.Redirection -> redirectToLocationApiPage()
            LocationUserInteraction.FindByQuery -> findTypedLocation()
            is LocationUserInteraction.ToggleMap -> toggleMap(interaction.isVisible)
            LocationUserInteraction.MyLocation -> setAction(LocationMainAction.MyLocation)
            LocationUserInteraction.Settings -> setAction(LocationMainAction.NavigateToSettings)
            LocationUserInteraction.OnBackClick -> setAction(LocationMainAction.NavigateBack)
            LocationUserInteraction.RequestLocationPermissions -> onRequestPermissions()
            is LocationUserInteraction.UpdateFavoriteLocation -> updateFavoriteLocation(interaction.favoriteLocationDto)
            is LocationUserInteraction.ToggleEditFavoriteLocationDialog -> onToggle(interaction.item)
            is LocationUserInteraction.SetDefaultLocationName -> setDefaultLocationName(interaction.item)
            LocationUserInteraction.DismissConfirmPinDialog -> onDismissConfirmPinDialog()
            LocationUserInteraction.NewLocationConfirm -> onNewLocationConfirm()
            is LocationUserInteraction.NewLocationPicked -> onNewLocationPicked(interaction.location)
            LocationUserInteraction.DismissDialog -> onDismissDialog()
            is LocationUserInteraction.EditLocationText -> onNewEditLocationText(interaction.input)
            is LocationUserInteraction.GeoInputQuery -> onNewGeoText(interaction.input)
            is LocationUserInteraction.ShowDeleteFavoriteLocation -> onShowDeleteFavoriteLocation(
                interaction.item
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
        setAction(LocationMainAction.RequestLocationPermissions)
        onDismissDialog()
    }

    private fun onConfirmLocation(geoData: GeoData) {
        setAction(
            LocationMainAction.NavigateToWeather(
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
            setAction(
                LocationMainAction.NavigateToWeather(
                    query = queryResult.query,
                )
            )
        } else {
            setAction(LocationMainAction.ShowSnackbar(queryResult))
        }
    }

    private fun navigateToWeatherWithDelay(latLng: UserLatLng) {
        launch {
            delay(200)
            setAction(LocationMainAction.NavigateToWeatherWithLatLng(latLng))
        }
    }

    private fun onFavoriteLocation(location: FavoriteLocation) {
        setAction(
            LocationMainAction.NavigateToWeather(
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
                setAction(LocationMainAction.NavigateToWeatherWithLatLng(userLatLng))
            }
            setState { copy(newPickedLocation = null, loadNewLocationWeather = false) }
        }
    }

    private fun initMapPin() {
        setState { copy(mapPinLocation = sharedPrefsProvider.loadForecastFromCache()?.location) }
    }

    private fun toggleMap(isShown: Boolean) {
        setAction(LocationMainAction.ToggleMap(isShown))
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