package com.desuzed.everyweather.presentation.ui.main

import com.desuzed.everyweather.presentation.ui.HourUi

data class WeatherMainUi(
    val mainInfo: WeatherMainInfoUi,
    val hourList: List<HourUi>,
    val detailCard: DetailCardMain,
)
