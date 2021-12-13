package com.desuzed.everyweather.data.network.dto.weatherApi.mappers

import com.desuzed.everyweather.model.model.ApiError
import com.desuzed.everyweather.data.network.dto.weatherApi.ErrorDtoWeatherApi

class ApiErrorMapper: EntityMapper<ErrorDtoWeatherApi, ApiError> {
    override fun mapFromEntity(entity: ErrorDtoWeatherApi): ApiError {
        return ApiError(ApiError.Error(entity.error!!.code, entity.error!!.message ))
    }

}