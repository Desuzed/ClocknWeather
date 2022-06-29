package com.desuzed.everyweather.view.ui.main

import com.desuzed.everyweather.view.ui.HourUi

data class WeatherMainUi(
    val mainInfo: WeatherMainInfoUi,
    val hourList: List<HourUi>,
    val detailCard: DetailCardMain,
)
