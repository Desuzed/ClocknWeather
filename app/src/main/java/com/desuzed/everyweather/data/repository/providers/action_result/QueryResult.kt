package com.desuzed.everyweather.data.repository.providers.action_result

data class QueryResult(
    val code: Int,
    val query: String = "",
    val actionType: ActionType = ActionType.OK
)