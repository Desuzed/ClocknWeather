package com.desuzed.everyweather.data.mapper

import com.desuzed.everyweather.data.network.dto.weatherApi.ErrorDtoWeatherApi
import com.desuzed.everyweather.domain.model.weather.ApiError
import com.desuzed.everyweather.util.EntityMapper

class ApiErrorMapper : EntityMapper<ErrorDtoWeatherApi, ApiError> {
    override fun mapFromEntity(entity: ErrorDtoWeatherApi): ApiError {
        return ApiError(ApiError.Error(entity.error!!.code, entity.error!!.message))
    }
}
