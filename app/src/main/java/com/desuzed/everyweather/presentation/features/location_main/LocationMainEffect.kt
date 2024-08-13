package com.desuzed.everyweather.presentation.features.location_main

import com.desuzed.everyweather.domain.model.result.QueryResult
import com.desuzed.everyweather.presentation.base.SideEffect

sealed interface LocationMainEffect : SideEffect {
    class ShowSnackbar(val queryResult: QueryResult) : LocationMainEffect
    data object FindUserLocation : LocationMainEffect
    class ToggleMap(val isVisible: Boolean) : LocationMainEffect
    data object NavigateToSettings : LocationMainEffect
    data object NavigateBack : LocationMainEffect
    data object RequestLocationPermissions : LocationMainEffect
}