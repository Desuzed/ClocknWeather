package com.desuzed.everyweather.data.network.dto

sealed class ApiTypeError{
    open class WeatherApi : ApiTypeError()
    open class OpenWeatherApi : ApiTypeError() //TODO for example
}