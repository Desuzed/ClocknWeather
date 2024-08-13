package com.desuzed.everyweather.presentation.features.main_activity

import com.desuzed.everyweather.presentation.base.State
import com.desuzed.everyweather.util.Constants.EMPTY_STRING

data class MainActivityState(
    val lang: String = EMPTY_STRING,
    val isInternetUnavailable: Boolean = false,
    val isLookingForLocation: Boolean = false,
) : State