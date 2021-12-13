package com.desuzed.everyweather.data.network

import android.content.res.Resources
import com.desuzed.everyweather.R

class ActionResultProviderImpl(private val resources: Resources) : ActionResultProvider {
    override fun parseCode(errorCode: Int): String {
        return when (errorCode) {
            1 -> resources.getString(R.string.saved)
            2 -> resources.getString(R.string.deleted)
            10 -> resources.getString(R.string.no_data_to_load)
            20 -> resources.getString(R.string.check_internet_connection)
            30 -> resources.getString(R.string.unknown_app_error)
            1002 -> resources.getString(R.string.api_key_error)
            1003 -> resources.getString(R.string.q_not_provided)
            1005 -> resources.getString(R.string.api_request_is_invalid)
            1006 -> resources.getString(R.string.no_location_found)
            in 2000..2008 -> resources.getString(R.string.api_key_error)
            else -> resources.getString(R.string.internal_app_error)
        }
    }
}

interface ActionResultProvider {
    fun parseCode(errorCode: Int): String

    companion object {
        const val NO_DATA = 10
        const val NO_INTERNET = 11
        const val UNKNOWN = 12
        const val INSERTED = 1
        const val DELETED = 2
        const val EMPTY_FIELD = 3
        const val FAIL = -1
    }
}

