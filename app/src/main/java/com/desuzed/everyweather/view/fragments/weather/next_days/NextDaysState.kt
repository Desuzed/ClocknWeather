package com.desuzed.everyweather.view.fragments.weather.next_days

import com.desuzed.everyweather.view.ui.NextDaysUi

data class NextDaysState(
    val nextDaysUiList: List<NextDaysUi> = emptyList(),
    val isExpanded : Boolean = false,
)