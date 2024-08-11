package com.desuzed.everyweather.data.repository.remote

import com.desuzed.everyweather.data.mapper.location.LocationResponseMapper
import com.desuzed.everyweather.data.mapper.weather_api.ApiErrorMapper
import com.desuzed.everyweather.data.mapper.weather_api.WeatherResponseMapper
import com.desuzed.everyweather.data.network.retrofit.api.LocationIqApi
import com.desuzed.everyweather.data.network.retrofit.api.WeatherApiService
import com.desuzed.everyweather.domain.model.location.geo.GeoData
import com.desuzed.everyweather.domain.model.network.NetworkResponse
import com.desuzed.everyweather.domain.model.result.FetchResult
import com.desuzed.everyweather.domain.model.weather.Error
import com.desuzed.everyweather.domain.model.weather.WeatherContent
import com.desuzed.everyweather.domain.repository.provider.ActionResultProvider.Companion.NO_INTERNET
import com.desuzed.everyweather.domain.repository.provider.ActionResultProvider.Companion.UNKNOWN
import com.desuzed.everyweather.domain.repository.remote.RemoteDataRepository
import com.desuzed.everyweather.util.Constants.EMPTY_STRING
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RemoteDataRepositoryImpl(
    private val weatherApi: WeatherApiService,
    private val locationIqApi: LocationIqApi,
    private val weatherResponseMapper: WeatherResponseMapper,
    private val locationResponseMapper: LocationResponseMapper,
    private val apiErrorMapper: ApiErrorMapper,
    private val dispatcher: CoroutineDispatcher,
) : RemoteDataRepository {

    override suspend fun getForecast(
        query: String,
        lang: String,
    ): FetchResult<WeatherContent, Error> = withContext(dispatcher) {
        when (val response = weatherApi.getForecast(query, lang)) {
            is NetworkResponse.Success -> {
                val weatherContent = weatherResponseMapper.mapFromEntity(response.body)
                FetchResult.Success(weatherContent)
            }

            is NetworkResponse.ApiError -> {
                val apiError = apiErrorMapper.mapFromEntity(response.body)
                FetchResult.Failure(apiError.error)
            }

            is NetworkResponse.NetworkError -> FetchResult.Failure(
                Error(code = NO_INTERNET, message = EMPTY_STRING)
            )


            is NetworkResponse.UnknownError -> FetchResult.Failure(
                Error(code = UNKNOWN, message = EMPTY_STRING)
            )
        }
    }

    override suspend fun forwardGeocoding(
        query: String,
        lang: String,
    ): FetchResult<List<GeoData>, Error> = withContext(dispatcher) {
        when (val response = locationIqApi.forwardGeocoding(query = query, lang = lang)) {
            is NetworkResponse.Success -> {
                val mappedResult = locationResponseMapper.mapFromEntity(response.body)
                FetchResult.Success(mappedResult)
            }

            is NetworkResponse.ApiError -> FetchResult.Failure(
                Error(code = response.code, message = EMPTY_STRING)
            )

            is NetworkResponse.NetworkError -> FetchResult.Failure(
                Error(code = NO_INTERNET, message = EMPTY_STRING)
            )

            is NetworkResponse.UnknownError -> FetchResult.Failure(
                Error(code = UNKNOWN, message = EMPTY_STRING)
            )
        }

    }

}