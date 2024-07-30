package com.desuzed.everyweather.domain.model.result

import java.util.UUID

data class QueryResult(
    val code: Int,
    val query: String = "",
    val actionType: ActionType = ActionType.OK,
    val id: String = UUID.randomUUID().toString(),
)