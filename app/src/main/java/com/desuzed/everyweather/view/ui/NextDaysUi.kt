package com.desuzed.everyweather.view.ui

import com.desuzed.everyweather.view.entity.HourUi

data class NextDaysUi(
    val iconUrl: String,
    val date: String,
    val description: String,
    val maxTemp: String,
    val minTemp: String,
    val detailCard : DetailCard,
    val hourList : List<HourUi>,
)