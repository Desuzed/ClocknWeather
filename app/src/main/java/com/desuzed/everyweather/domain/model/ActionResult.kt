package com.desuzed.everyweather.domain.model

data class ActionResult(
    val message: String,
    val messageId: Int? = null,
    val actionType: ActionType = ActionType.OK
)

enum class ActionType {
    OK, RETRY
}