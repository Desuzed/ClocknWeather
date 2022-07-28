package com.desuzed.everyweather.presentation.features.weather_next_days

import com.desuzed.everyweather.presentation.ui.next_days.NextDaysUi

data class NextDaysState(
    val nextDaysUiList: List<NextDaysUi> = emptyList(),
    val isExpanded : Boolean = false,
)