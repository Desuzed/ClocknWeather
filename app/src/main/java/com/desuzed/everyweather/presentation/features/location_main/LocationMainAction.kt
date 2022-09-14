package com.desuzed.everyweather.presentation.features.location_main

import com.desuzed.everyweather.domain.model.ActionResult


sealed interface LocationMainAction {
    class ShowSnackbar(val action: ActionResult) : LocationMainAction
    class NavigateToWeather(val query: String, val key: String) : LocationMainAction
    object MyLocation : LocationMainAction
    object ShowMapFragment : LocationMainAction
    object NavigateToSettings : LocationMainAction
    object NavigateBack : LocationMainAction
}