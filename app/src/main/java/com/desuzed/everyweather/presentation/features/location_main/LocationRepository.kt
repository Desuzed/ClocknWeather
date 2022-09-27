package com.desuzed.everyweather.presentation.features.location_main

import com.desuzed.everyweather.data.mapper.LocationResponseMapper
import com.desuzed.everyweather.data.network.retrofit.NetworkResponse
import com.desuzed.everyweather.data.repository.providers.action_result.ActionResultProvider
import com.desuzed.everyweather.domain.model.location.geo.ResultGeo
import com.desuzed.everyweather.domain.repository.remote.RemoteDataSource

class LocationRepository(
    private val remoteDataSource: RemoteDataSource,
    private val actionResultProvider: ActionResultProvider,
) {
    suspend fun fetchGeocodingResultOrError(query: String, lang: String): ResultGeo {
        val response = remoteDataSource.forwardGeocoding(query, lang)
        return when (response) {
            is NetworkResponse.Success -> {
                val mappedResult = LocationResponseMapper().mapFromEntity(response.body)
                ResultGeo(mappedResult, null)
            }
            is NetworkResponse.ApiError -> {
                ResultGeo(
                    null,
                    actionResultProvider.parseCode(errorCode = response.code, query = query)
                )
            }
            is NetworkResponse.NetworkError -> {
                ResultGeo(null, actionResultProvider.parseCode(ActionResultProvider.NO_INTERNET))
            }
            is NetworkResponse.UnknownError -> {
                ResultGeo(null, actionResultProvider.parseCode(ActionResultProvider.UNKNOWN))
            }
        }
    }
}