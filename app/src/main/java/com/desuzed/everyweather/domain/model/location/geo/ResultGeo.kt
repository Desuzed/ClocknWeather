package com.desuzed.everyweather.domain.model.location.geo

import com.desuzed.everyweather.domain.model.ActionResult

data class ResultGeo(
    val geoResponse: List<GeoResponse>?,
    val actionResult: ActionResult?
)