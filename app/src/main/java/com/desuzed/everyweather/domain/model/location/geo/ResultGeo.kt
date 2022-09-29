package com.desuzed.everyweather.domain.model.location.geo

import com.desuzed.everyweather.data.repository.providers.action_result.QueryResult

data class ResultGeo(
    val geoResponse: List<GeoResponse>?,
    val queryResult: QueryResult?
)