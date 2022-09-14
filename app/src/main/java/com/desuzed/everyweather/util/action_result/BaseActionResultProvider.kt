package com.desuzed.everyweather.util.action_result

import android.content.res.Resources
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.ActionResult
import com.desuzed.everyweather.domain.model.ActionType
import com.desuzed.everyweather.util.action_result.ActionResultProvider.Companion.DELETED
import com.desuzed.everyweather.util.action_result.ActionResultProvider.Companion.NO_DATA
import com.desuzed.everyweather.util.action_result.ActionResultProvider.Companion.NO_INTERNET
import com.desuzed.everyweather.util.action_result.ActionResultProvider.Companion.SAVED
import com.desuzed.everyweather.util.action_result.ActionResultProvider.Companion.UNKNOWN

abstract class BaseActionResultProvider(private val resources: Resources) : ActionResultProvider {
    override fun parseCode(errorCode: Int, query: String): ActionResult {
        return when (errorCode) {
            SAVED -> ActionResult(message = resources.getString(R.string.saved))
            DELETED -> ActionResult(message = resources.getString(R.string.deleted))
            NO_DATA -> ActionResult(message = resources.getString(R.string.no_data_to_load))
            NO_INTERNET -> ActionResult(
                message = resources.getString(R.string.check_internet_connection),
                actionType = ActionType.RETRY
            )
            UNKNOWN -> ActionResult(
                message = resources.getString(R.string.unknown_app_error),
                actionType = ActionType.RETRY
            )

            else -> ActionResult(
                message = resources.getString(R.string.internal_app_error),
                actionType = ActionType.RETRY
            )
        }
    }
}

interface ActionResultProvider {
    fun parseCode(errorCode: Int, query: String = ""): ActionResult

    companion object {
        const val NO_DATA = 10
        const val NO_INTERNET = 11
        const val UNKNOWN = 12
        const val SAVED = 1
        const val DELETED = 2
        const val FAIL = -1
    }
}
