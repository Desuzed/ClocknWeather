package com.desuzed.everyweather.domain.model.result

data class QueryResult(
    val code: Int,
    val query: String = "",
    val actionType: ActionType = ActionType.OK
)