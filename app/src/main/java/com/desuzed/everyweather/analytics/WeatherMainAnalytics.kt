package com.desuzed.everyweather.analytics

import android.content.Context
import com.desuzed.everyweather.presentation.features.weather_main.WeatherUserInteraction

class WeatherMainAnalytics(context: Context) : Analytics(context = context) {

    fun onUserInteraction(interaction: WeatherUserInteraction) {
        when (interaction) {
            WeatherUserInteraction.Location -> logEvent(NAVIGATE_TO_LOCATION)
            WeatherUserInteraction.NextDays -> logEvent(NAVIGATE_TO_NEXT_DAYS)
            WeatherUserInteraction.SaveLocation -> logEvent(SAVE_LOCATION)
            else -> {}
        }
    }

    companion object {
        private const val NAVIGATE_TO_LOCATION = "weather_main_nav_to_location"
        private const val NAVIGATE_TO_NEXT_DAYS = "weather_main_nav_to_next"
        private const val SAVE_LOCATION = "weather_main_save"
    }
}