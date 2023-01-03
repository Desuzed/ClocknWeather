package com.desuzed.everyweather.data.repository.providers.action_result

import android.content.res.Resources
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.repository.provider.BaseActionResultProvider

class GeoActionResultProvider(resources: Resources) : BaseActionResultProvider(resources) {
    override fun parseCode(errorCode: Int, query: String): String {
        return when (errorCode) {
            MISSING_PARAMS -> getString(R.string.no_location_found, query)
            INVALID_TOKEN -> getString(R.string.geo_api_error)
            ACCESS_RESTRICTED -> getString(R.string.geo_api_error)
            RATE_LIMIT -> getString(R.string.retry_again)
            UNABLE_TO_GEOCODE -> getString(R.string.no_location_found, query)
            NO_LOCATION_PERMISSIONS -> getString(R.string.location_permissions_are_not_granted)
            LOCATION_NOT_FOUND -> getString(R.string.your_current_location_not_found)
            else -> super.parseCode(errorCode, query)
        }
    }

    companion object {
        private const val MISSING_PARAMS = 400
        private const val INVALID_TOKEN = 401
        private const val ACCESS_RESTRICTED = 403
        private const val UNABLE_TO_GEOCODE = 404
        const val RATE_LIMIT = 429
        const val NO_LOCATION_PERMISSIONS = 900
        const val LOCATION_NOT_FOUND = 901

    }
}