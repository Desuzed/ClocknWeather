package com.desuzed.everyweather.presentation.features.location_main

import androidx.lifecycle.viewModelScope
import com.desuzed.everyweather.data.repository.local.SettingsRepository
import com.desuzed.everyweather.data.room.FavoriteLocationDto
import com.desuzed.everyweather.domain.model.ActionResult
import com.desuzed.everyweather.domain.model.location.geo.GeoResponse
import com.desuzed.everyweather.domain.model.settings.Language
import com.desuzed.everyweather.domain.repository.local.RoomProvider
import com.desuzed.everyweather.presentation.base.BaseViewModel
import com.desuzed.everyweather.presentation.features.weather_main.WeatherMainFragment
import com.desuzed.everyweather.util.action_result.ActionResultProvider
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class LocationViewModel(
    private val roomProvider: RoomProvider,
    private val actionResultProvider: ActionResultProvider,
    private val locationUseCase: LocationUseCase,
    settingsRepository: SettingsRepository,
) :
    BaseViewModel<LocationMainState, LocationMainAction>(LocationMainState()) {

    private val actionResultFlow = MutableSharedFlow<ActionResult>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        collect(roomProvider.getAllLocations(), ::onNewLocations)
        collect(settingsRepository.lang, ::collectLanguage)
        collect(actionResultFlow, ::collectActionResult)
    }

    fun onNewGeoText(text: String) {
        setState { copy(geoText = text) }
    }

    fun onUserInteraction(userInteraction: LocationUserInteraction) {
        when (userInteraction) {
            is LocationUserInteraction.DeleteFavoriteLocation -> deleteFavoriteLocation(
                userInteraction.favoriteLocationDto
            )
            is LocationUserInteraction.ConfirmFoundLocation -> onConfirmLocation(userInteraction.geo)
            is LocationUserInteraction.FavoriteLocation -> setAction(
                LocationMainAction.NavigateToWeather(
                    query = userInteraction.favoriteLocationDto.toQuery(),
                    key = WeatherMainFragment.QUERY_KEY
                )
            )
            is LocationUserInteraction.Redirection -> setAction(
                LocationMainAction.ShowSnackbar(
                    ActionResult(message = userInteraction.message)
                )
            )
            LocationUserInteraction.FindByQuery -> findTypedLocation()
            LocationUserInteraction.FindOnMap -> setAction(LocationMainAction.ShowMapFragment)
            LocationUserInteraction.MyLocation -> setAction(LocationMainAction.MyLocation)
            LocationUserInteraction.Settings -> setAction(LocationMainAction.NavigateToSettings)
            LocationUserInteraction.OnBackClick -> setAction(LocationMainAction.NavigateBack)
            LocationUserInteraction.DismissLocationPicker -> setState { copy(showPickerDialog = false) }
        }
    }

    private fun findTypedLocation() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            val resultGeo = locationUseCase.fetchGeocodingResultOrError(
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
            val resultAction = resultGeo.actionResult
            if (resultAction != null) {
                actionResultFlow.emit(resultAction)
            }
        }

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

    private fun onSuccess(code: Int) {
        val message = actionResultProvider.parseCode(code).message
        setAction(LocationMainAction.ShowSnackbar(ActionResult(message = message)))
    }

    private fun onError(code: Int) {
        val message = actionResultProvider.parseCode(code).message
        setAction(LocationMainAction.ShowSnackbar(ActionResult(message = message)))
    }

    private fun onNewLocations(locationsList: List<FavoriteLocationDto>) {
        setState { copy(locations = locationsList) }
    }

    private fun collectLanguage(language: Language) = setState { copy(lang = language) }

    private fun collectActionResult(actionResult: ActionResult) =
        setAction(LocationMainAction.ShowSnackbar(actionResult))
}