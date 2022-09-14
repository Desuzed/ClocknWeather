package com.desuzed.everyweather.data.network.retrofit.api

import com.desuzed.everyweather.BuildConfig
import com.desuzed.everyweather.data.network.dto.location_iq.LocationIqError
import com.desuzed.everyweather.data.network.dto.location_iq.LocationResult
import com.desuzed.everyweather.data.network.retrofit.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationIqApi {
    @GET("search?key=${BuildConfig.LOCATION_IQ_API}&format=json")
    suspend fun forwardGeocoding(
        @Query("q") query: String,
        @Query("accept-language") lang: String,
    ): NetworkResponse<List<LocationResult>, LocationIqError>

    @GET("reverse?key=${BuildConfig.LOCATION_IQ_API}&format=json")
    suspend fun reverseGeocoding(
        @Query("lat") lat: String,
        @Query("lng") lng: String,
        @Query("accept-language") lang: String,
    ): NetworkResponse<List<LocationResult>, LocationIqError>
}