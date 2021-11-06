package com.desuzed.clocknweather.network

import android.content.Context
import com.desuzed.clocknweather.R

class ErrorHandler(val context: Context) {
    fun getApiError (errorCode : Int?) : String{
        return when (errorCode){
            1002 -> context.resources.getString(R.string.api_key_error)
            1003 -> context.resources.getString(R.string.q_not_provided)
            1005 -> context.resources.getString(R.string.api_request_is_invalid)
            1006 -> context.resources.getString(R.string.no_location_found)
            in 2000..2008 -> context.resources.getString(R.string.api_key_error)
            else -> context.resources.getString(R.string.internal_app_error)
        }
    }

    fun noDataToLoad () : String{
        return context.resources.getString(R.string.no_data_to_load)
    }

    fun getInternetError () : String{
          return context.resources.getString(R.string.check_internet_connection)
    }

    fun getUnknownError () : String{
        return context.resources.getString(R.string.unknown_app_error)
    }
}