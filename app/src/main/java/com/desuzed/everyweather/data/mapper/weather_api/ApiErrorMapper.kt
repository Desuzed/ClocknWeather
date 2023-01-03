package com.desuzed.everyweather.data.mapper.weather_api

import com.desuzed.everyweather.data.mapper.EntityMapper
import com.desuzed.everyweather.data.network.dto.weatherApi.ErrorDtoWeatherApi
import com.desuzed.everyweather.domain.model.weather.ApiError
import com.desuzed.everyweather.domain.model.weather.Error

class ApiErrorMapper : EntityMapper<ErrorDtoWeatherApi, ApiError> {
    override fun mapFromEntity(entity: ErrorDtoWeatherApi): ApiError {
        return ApiError(
            error = Error(
                code = entity.error?.code ?: 0,
                message = entity.error?.message ?: ""
            )
        )
    }
}
