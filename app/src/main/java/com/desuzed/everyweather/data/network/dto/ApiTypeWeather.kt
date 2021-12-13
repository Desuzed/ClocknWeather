package com.desuzed.everyweather.data.network.dto

sealed class ApiTypeWeather{
    open class WeatherApi : ApiTypeWeather()
    open class OpenWeatherApi : ApiTypeWeather() //TODO for example
}