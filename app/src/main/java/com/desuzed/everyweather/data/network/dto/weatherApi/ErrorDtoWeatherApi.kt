package com.desuzed.everyweather.data.network.dto.weatherApi

import com.desuzed.everyweather.data.network.dto.ApiTypeError
import com.desuzed.everyweather.data.network.dto.EntityMapper
import com.desuzed.everyweather.model.model.ApiError
import com.google.gson.annotations.SerializedName


class ErrorDtoWeatherApi : ApiTypeError.WeatherApi() {
    @SerializedName("error")
    var error : Error? = null
    class Error {
        @SerializedName("code")
        var code = 0
        @SerializedName("message")
        var message: String = ""
    }
}
class ApiErrorMapper: EntityMapper<ErrorDtoWeatherApi, ApiError> {
    override fun mapFromEntity(entity: ErrorDtoWeatherApi): ApiError {
        return ApiError(ApiError.Error(entity.error!!.code, entity.error!!.message ))
    }
}

