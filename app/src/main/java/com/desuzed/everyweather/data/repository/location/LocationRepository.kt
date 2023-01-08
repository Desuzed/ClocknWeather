package com.desuzed.everyweather.data.repository.location

import com.desuzed.everyweather.data.mapper.location.LocationResponseMapper
import com.desuzed.everyweather.domain.model.location.geo.ResultGeo
import com.desuzed.everyweather.domain.model.network.NetworkResponse
import com.desuzed.everyweather.domain.model.result.ActionType
import com.desuzed.everyweather.domain.model.result.QueryResult
import com.desuzed.everyweather.domain.repository.provider.ActionResultProvider
import com.desuzed.everyweather.domain.repository.remote.RemoteDataSource

class LocationRepository(private val remoteDataSource: RemoteDataSource) {
    suspend fun fetchGeocodingResultOrError(query: String, lang: String): ResultGeo {
        val response = remoteDataSource.forwardGeocoding(query, lang)
        return when (response) {
            is NetworkResponse.Success -> {
                val mappedResult = LocationResponseMapper().mapFromEntity(response.body)
                ResultGeo(mappedResult, null)
            }
            is NetworkResponse.ApiError -> {
                ResultGeo(
                    geoResponse = null,
                    queryResult = QueryResult(
                        code = response.code,
                        query = query,
                        actionType = ActionType.RETRY,
                    ),
                )
            }
            is NetworkResponse.NetworkError -> {
                ResultGeo(
                    geoResponse = null,
                    QueryResult(
                        code = ActionResultProvider.NO_INTERNET,
                        query = query,
                        actionType = ActionType.RETRY,
                    ),
                )
            }
            is NetworkResponse.UnknownError -> {
                ResultGeo(
                    geoResponse = null,
                    QueryResult(
                        code = ActionResultProvider.UNKNOWN,
                        query = query,
                        actionType = ActionType.RETRY,
                    ),
                )
            }
        }
    }
}