package com.desuzed.everyweather.network

import android.content.Context
import android.content.res.Resources
import com.desuzed.everyweather.R

class NetworkErrorHandler(private val resources: Resources) {
    fun getApiError(errorCode: Int?): String {
        return when (errorCode) {
            1002 -> resources.getString(R.string.api_key_error)
            1003 -> resources.getString(R.string.q_not_provided)
            1005 -> resources.getString(R.string.api_request_is_invalid)
            1006 -> resources.getString(R.string.no_location_found)
            in 2000..2008 -> resources.getString(R.string.api_key_error)
            else -> resources.getString(R.string.internal_app_error)
        }
    }

    fun noDataToLoad(): String {
        return resources.getString(R.string.no_data_to_load)
    }

    fun getInternetError(): String {
        return resources.getString(R.string.check_internet_connection)
    }

    fun getUnknownError(): String {
        return resources.getString(R.string.unknown_app_error)
    }

}