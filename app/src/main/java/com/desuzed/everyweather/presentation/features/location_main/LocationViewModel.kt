package com.desuzed.everyweather.presentation.features.location_main

import androidx.lifecycle.viewModelScope
import com.desuzed.everyweather.analytics.LocationMainAnalytics
import com.desuzed.everyweather.data.repository.local.SettingsDataStore
import com.desuzed.everyweather.data.repository.providers.UserLocationProvider
import com.desuzed.everyweather.data.room.FavoriteLocationDto
import com.desuzed.everyweather.domain.model.location.geo.GeoResponse
import com.desuzed.everyweather.domain.model.result.QueryResult
import com.desuzed.everyweather.domain.model.settings.Language
import com.desuzed.everyweather.domain.repository.local.RoomProvider
import com.desuzed.everyweather.domain.repository.provider.ActionResultProvider
import com.desuzed.everyweather.presentation.base.BaseViewModel
import com.desuzed.everyweather.presentation.features.weather_main.WeatherMainFragment
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class LocationViewModel(
    private val roomProvider: RoomProvider,
    private val locationRepository: LocationRepository,
    private val userLocationProvider: UserLocationProvider,
    private val analytics: LocationMainAnalytics,
    settingsDataStore: SettingsDataStore,
) : BaseViewModel<LocationMainState, LocationMainAction, LocationUserInteraction>(LocationMainState()) {

    private val queryResultFlow = MutableSharedFlow<QueryResult>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        collect(roomProvider.getAllLocations(), ::onNewLocations)
        collect(settingsDataStore.lang, ::collectLanguage)
        collect(queryResultFlow, ::collectQueryResult)
    }

    fun onNewGeoText(text: String) {
        setState { copy(geoText = text) }
    }

    override fun onUserInteraction(interaction: LocationUserInteraction) {
        analytics.onUserInteraction(interaction)
        when (interaction) {
            is LocationUserInteraction.DeleteFavoriteLocation -> deleteFavoriteLocation(
                interaction.favoriteLocationDto
            )
            is LocationUserInteraction.ConfirmFoundLocation -> onConfirmLocation(interaction.geo)
            is LocationUserInteraction.FavoriteLocation -> setAction(
                LocationMainAction.NavigateToWeather(
                    query = interaction.favoriteLocationDto.toQuery(),
                    key = WeatherMainFragment.QUERY_KEY
                )
            )
            LocationUserInteraction.Redirection -> viewModelScope.launch {
                queryResultFlow.emit(QueryResult(code = ActionResultProvider.REDIRECTION))
            }
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
        }
    }

    fun areLocationPermissionsGranted(): Boolean = userLocationProvider.arePermissionsGranted()

    fun launchRequireLocationPermissionsDialog() {
        setState { copy(showRequireLocationPermissionsDialog = true) }
    }

    private fun findTypedLocation() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            val resultGeo = locationRepository.fetchGeocodingResultOrError(
                state.value.geoText,
                state.value.lang.id.lowercase()
            )
            setState {
                copy(
                    geoResponses = resultGeo.geoResponse,
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

    private fun onConfirmLocation(geoResponse: GeoResponse) {
        setAction(
            LocationMainAction.NavigateToWeather(
                query = "${geoResponse.lat},${geoResponse.lon}",
                key = WeatherMainFragment.QUERY_KEY
            )
        )
        setState { copy(geoText = "", geoResponses = null) }
    }

    private fun deleteFavoriteLocation(favoriteLocationDto: FavoriteLocationDto) =
        viewModelScope.launch {
            val deleted = roomProvider.deleteItem(favoriteLocationDto)
            if (deleted) onSuccess(ActionResultProvider.DELETED)
            else onError(ActionResultProvider.FAIL)
        }

    private suspend fun onSuccess(code: Int) {
        queryResultFlow.emit(QueryResult(code))
    }

    private suspend fun onError(code: Int) {
        queryResultFlow.emit(QueryResult(code))
    }

    private fun onNewLocations(locationsList: List<FavoriteLocationDto>) {
        setState { copy(locations = locationsList) }
    }

    private fun collectLanguage(language: Language) = setState { copy(lang = language) }

    private fun collectQueryResult(queryResult: QueryResult) =
        setAction(LocationMainAction.ShowSnackbar(queryResult))

}