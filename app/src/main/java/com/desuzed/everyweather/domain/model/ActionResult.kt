package com.desuzed.everyweather.domain.model

data class ActionResult(
    val message: String,
    val actionType: ActionType = ActionType.OK
)

enum class ActionType {
    OK, RETRY
}