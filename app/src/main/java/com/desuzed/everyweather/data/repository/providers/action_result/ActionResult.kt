package com.desuzed.everyweather.data.repository.providers.action_result

data class ActionResult(
    val code: Int,
    val actionType: ActionType = ActionType.OK
)

enum class ActionType {
    OK, RETRY
}