package com.desuzed.everyweather.domain.model.location

import com.desuzed.everyweather.domain.model.ActionResult
import com.desuzed.everyweather.domain.model.UserLatLng

class UserLocationResult(
    val userLatLng: UserLatLng? = null,
    val actionResult: ActionResult? = null
)
