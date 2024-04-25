package com.desuzed.everyweather.presentation.features.location_main

import com.desuzed.everyweather.analytics.LocationMainAnalytics
import com.desuzed.everyweather.data.repository.providers.UserLocationProvider
import com.desuzed.everyweather.data.repository.providers.action_result.GeoActionResultProvider
import com.desuzed.everyweather.domain.interactor.LocationInteractor
import com.desuzed.everyweather.domain.model.location.FavoriteLocation
import com.desuzed.everyweather.domain.model.location.UserLatLng
import com.desuzed.everyweather.domain.model.location.geo.GeoData
import com.desuzed.everyweather.domain.model.result.QueryResult
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
) : BaseViewModel<LocationMainState, LocationMainAction, LocationUserInteraction>(LocationMainState()) {

    private val queryResultFlow = MutableSharedFlow<QueryResult>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        collect(locationInteractor.getAllLocations(), ::onNewLocations)
        collect(queryResultFlow, ::collectQueryResult)
    }

    fun onNewGeoText(text: String) {
        setState { copy(geoText = text) }
    }

    fun onNewEditLocationText(text: String) {
        setState { copy(editLocationText = text) }
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
            LocationUserInteraction.FindOnMap -> setAction(LocationMainAction.ShowMapFragment)
            LocationUserInteraction.MyLocation -> setAction(LocationMainAction.MyLocation)
            LocationUserInteraction.Settings -> setAction(LocationMainAction.NavigateToSettings)
            LocationUserInteraction.OnBackClick -> setAction(LocationMainAction.NavigateBack)
            LocationUserInteraction.DismissLocationPicker -> setState { copy(showPickerDialog = false) }
            LocationUserInteraction.DismissLocationPermissionsDialog -> setState {
                copy(showRequireLocationPermissionsDialog = false)
            }

            LocationUserInteraction.RequestLocationPermissions -> onRequestPermissions()
            is LocationUserInteraction.UpdateFavoriteLocation -> updateFavoriteLocation(interaction.favoriteLocationDto)
            is LocationUserInteraction.ToggleEditFavoriteLocationDialog -> setState {
                copy(
                    showEditLocationDialog = interaction.item,
                    editLocationText = interaction.item?.customName
                        ?.ifEmpty { interaction.item.cityName } ?: EMPTY_STRING,
                )
            }

            is LocationUserInteraction.SetDefaultLocationName -> setDefaultLocationName(interaction.item)
        }
    }

    fun areLocationPermissionsGranted(): Boolean = userLocationProvider.arePermissionsGranted()

    fun launchRequireLocationPermissionsDialog() {
        setState { copy(showRequireLocationPermissionsDialog = true) }
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
                    showPickerDialog = true
                )
            }
            val resultAction = resultGeo.queryResult
            if (resultAction != null) {
                queryResultFlow.emit(resultAction)
            }
        }

    }

    private fun onRequestPermissions() {
        setAction(LocationMainAction.RequestLocationPermissions)
        setState { copy(showRequireLocationPermissionsDialog = false) }
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
            val deleted = locationInteractor.deleteFavoriteLocation(favoriteLocationDto)
            if (deleted) {
                onSuccess(ActionResultProvider.DELETED)
            } else {
                onError(ActionResultProvider.FAIL)
            }
        }

    private fun updateFavoriteLocation(favoriteLocationDto: FavoriteLocation) {
        launch {
            setState { copy(showEditLocationDialog = null) }
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
            setState { copy(showEditLocationDialog = null) }
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

}