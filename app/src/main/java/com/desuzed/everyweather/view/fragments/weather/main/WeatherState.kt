package com.desuzed.everyweather.view.fragments.weather.main

import com.desuzed.everyweather.model.entity.WeatherResponse

data class WeatherState (
    val weatherData : WeatherResponse? = null,
    val isAddButtonEnabled: Boolean = false,
    val query : String = "",
    val isLoading : Boolean = false,
    val infoMessage : String = "",
)