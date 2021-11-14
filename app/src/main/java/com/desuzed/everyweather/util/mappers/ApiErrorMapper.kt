package com.desuzed.everyweather.util.mappers

import com.desuzed.everyweather.mvvm.model.ApiError
import com.desuzed.everyweather.network.dto.ApiErrorDto

class ApiErrorMapper: EntityMapper<ApiErrorDto, ApiError> {
    override fun mapFromEntity(entity: ApiErrorDto): ApiError {
        return ApiError(ApiError.Error(entity.error!!.code, entity.error!!.message ))
    }

}