package com.desuzed.everyweather.domain.repository.remote

import com.desuzed.everyweather.data.network.dto.location_iq.LocationIqError
import com.desuzed.everyweather.data.network.dto.location_iq.LocationResult
import com.desuzed.everyweather.data.network.dto.weatherApi.ErrorDtoWeatherApi
import com.desuzed.everyweather.data.network.dto.weatherApi.WeatherResponseDto
import com.desuzed.everyweather.domain.model.network.NetworkResponse

interface RemoteDataSource {
    suspend fun getForecast(
        query: String,
        lang: String
    ): NetworkResponse<WeatherResponseDto, ErrorDtoWeatherApi>

    suspend fun forwardGeocoding(
        query: String,
        lang: String
    ): NetworkResponse<List<LocationResult>, LocationIqError>

    suspend fun reverseGeocoding(
        lat: String,
        lng: String,
        lang: String
    ): NetworkResponse<List<LocationResult>, LocationIqError>
}