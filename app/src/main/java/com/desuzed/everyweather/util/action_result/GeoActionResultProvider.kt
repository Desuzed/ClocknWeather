package com.desuzed.everyweather.util.action_result

import android.content.res.Resources
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.ActionResult
import com.desuzed.everyweather.domain.model.ActionType

class GeoActionResultProvider(
    private val resources: Resources
) : BaseActionResultProvider(resources) {
    override fun parseCode(errorCode: Int, query: String): ActionResult {
        return when (errorCode) {
            MISSING_PARAMS -> ActionResult(
                message = resources.getString(R.string.no_location_found, query),
                actionType = ActionType.RETRY
            )
            INVALID_TOKEN -> ActionResult(
                message = resources.getString(R.string.geo_api_error),
                actionType = ActionType.RETRY
            )
            ACCESS_RESTRICTED -> ActionResult(
                message = resources.getString(R.string.geo_api_error),
                actionType = ActionType.RETRY
            )
            RATE_LIMIT -> ActionResult(
                message = resources.getString(R.string.retry_again),
                actionType = ActionType.RETRY
            )
            UNABLE_TO_GEOCODE -> ActionResult(
                message = resources.getString(R.string.no_location_found, query),
                actionType = ActionType.RETRY
            )
            else -> super.parseCode(errorCode, query)
        }
    }

    companion object {
        private const val MISSING_PARAMS = 400
        private const val INVALID_TOKEN = 401
        private const val ACCESS_RESTRICTED = 403
        private const val UNABLE_TO_GEOCODE = 404
        private const val RATE_LIMIT = 429
    }
}