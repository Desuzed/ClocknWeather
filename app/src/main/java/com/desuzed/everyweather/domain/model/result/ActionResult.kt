package com.desuzed.everyweather.domain.model.result

data class ActionResult(
    val code: Int,
    val actionType: ActionType = ActionType.OK
)

enum class ActionType {
    OK, RETRY
}