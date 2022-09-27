package com.desuzed.everyweather.analytics

import android.content.Context
import com.desuzed.everyweather.presentation.features.locatation_map.MapUserInteraction

class MapLocationAnalytics(context: Context) : Analytics(context = context) {

    fun onUserInteraction(interaction: MapUserInteraction) {
        when (interaction) {
            MapUserInteraction.NewLocationConfirm -> logEvent(CONFIRM_MAP_LOCATION)
            else -> {}
        }
    }

    companion object {
        private const val CONFIRM_MAP_LOCATION = "map_loc_confirm"

    }
}