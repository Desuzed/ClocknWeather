package com.desuzed.everyweather.data.repository.providers.action_result

import android.content.res.Resources
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.ActionResult
import com.desuzed.everyweather.domain.model.ActionType

class WeatherActionResultProvider(private val resources: Resources) :
    BaseActionResultProvider(resources) {
    override fun parseCode(errorCode: Int, query: String): ActionResult {
        return when (errorCode) {
            500, 501 -> ActionResult(
                message = resources.getString(R.string.wrong_server_result),
                ActionType.RETRY
            )
            in 502..530 -> ActionResult(
                message = resources.getString(R.string.server_error),
                actionType = ActionType.RETRY
            )
            1002 -> ActionResult(message = resources.getString(R.string.api_key_error))
            1003 -> ActionResult(message = resources.getString(R.string.q_not_provided))
            1005 -> ActionResult(message = resources.getString(R.string.api_request_is_invalid))
            1006 -> ActionResult(
                message = resources.getString(R.string.no_location_found, query),
                actionType = ActionType.RETRY
            )
            in 2000..2008 -> ActionResult(message = resources.getString(R.string.api_key_error))
            else -> super.parseCode(errorCode, query)
        }
    }
}