package com.desuzed.everyweather.data.repository.providers.action_result

import android.content.res.Resources
import com.desuzed.everyweather.R

class WeatherActionResultProvider(resources: Resources) :
    BaseActionResultProvider(resources) {
    override fun parseCode(errorCode: Int, query: String): String {
        return when (errorCode) {
            500, 501 -> getString(R.string.wrong_server_result)
            in 502..530 -> getString(R.string.server_error)
            1002 -> getString(R.string.api_key_error)
            1003 -> getString(R.string.q_not_provided)
            1005 -> getString(R.string.api_request_is_invalid)
            1006 -> getString(R.string.no_location_found, query)
            in 2000..2008 -> getString(R.string.api_key_error)
            else -> super.parseCode(errorCode, query)
        }
    }

}