package com.desuzed.everyweather.analytics

import android.content.Context
import com.desuzed.everyweather.presentation.features.weather_main.WeatherAction

class WeatherMainAnalytics(context: Context) : Analytics(context = context) {

    fun onAction(action: WeatherAction) {
        when (action) {
            WeatherAction.Location -> logEvent(NAVIGATE_TO_LOCATION)
            WeatherAction.SaveLocation -> logEvent(SAVE_LOCATION)
            else -> {}
        }
    }

    companion object {
        private const val NAVIGATE_TO_LOCATION = "weather_main_nav_to_location"

        //TODO
        private const val NAVIGATE_TO_NEXT_DAYS = "weather_main_nav_to_next"
        private const val SAVE_LOCATION = "weather_main_save"
    }
}