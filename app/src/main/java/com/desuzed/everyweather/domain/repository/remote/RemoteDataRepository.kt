package com.desuzed.everyweather.domain.repository.remote

import com.desuzed.everyweather.domain.model.location.geo.GeoData
import com.desuzed.everyweather.domain.model.result.FetchResult
import com.desuzed.everyweather.domain.model.weather.Error
import com.desuzed.everyweather.domain.model.weather.WeatherContent

interface RemoteDataRepository {
    suspend fun getForecast(
        query: String,
        lang: String
    ): FetchResult<WeatherContent, Error>

    suspend fun forwardGeocoding(
        query: String,
        lang: String
    ): FetchResult<List<GeoData>, Error>
//
//    suspend fun reverseGeocoding(
//        lat: String,
//        lng: String,
//        lang: String
//    ): NetworkResponse<List<GeoData>, LocationIqError>
}