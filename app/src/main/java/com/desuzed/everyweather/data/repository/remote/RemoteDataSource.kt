package com.desuzed.everyweather.data.repository.remote

import com.desuzed.everyweather.data.network.dto.location_iq.LocationIqError
import com.desuzed.everyweather.data.network.dto.location_iq.LocationResult
import com.desuzed.everyweather.data.network.dto.weatherApi.ErrorDtoWeatherApi
import com.desuzed.everyweather.data.network.dto.weatherApi.WeatherResponseDto
import com.desuzed.everyweather.data.network.retrofit.api.LocationIqApi
import com.desuzed.everyweather.data.network.retrofit.api.WeatherApiService
import com.desuzed.everyweather.domain.model.network.NetworkResponse
import com.desuzed.everyweather.domain.repository.remote.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteDataSourceImpl(
    private val weatherApi: WeatherApiService,
    private val locationIqApi: LocationIqApi,
) : RemoteDataSource {

    override suspend fun getForecast(
        query: String,
        lang: String
    ): NetworkResponse<WeatherResponseDto, ErrorDtoWeatherApi> =
        withContext(Dispatchers.IO) {
            weatherApi.getForecast(query, lang)
        }

    override suspend fun forwardGeocoding(
        query: String,
        lang: String
    ): NetworkResponse<List<LocationResult>, LocationIqError> =
        withContext(Dispatchers.IO) {
            locationIqApi.forwardGeocoding(query = query, lang = lang)
        }

    override suspend fun reverseGeocoding(
        lat: String,
        lng: String,
        lang: String
    ): NetworkResponse<List<LocationResult>, LocationIqError> = withContext(Dispatchers.IO) {
        locationIqApi.reverseGeocoding(lat = lat, lng = lng, lang = lang)
    }

}