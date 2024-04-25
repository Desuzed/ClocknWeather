package com.desuzed.everyweather.domain.model.location.geo

import com.desuzed.everyweather.domain.model.result.QueryResult

data class ResultGeo(
    val geoData: List<GeoData>?,
    val queryResult: QueryResult?
)