package com.desuzed.everyweather.data.repository

import com.desuzed.everyweather.util.ActionResultProvider
import com.desuzed.everyweather.data.network.dto.weatherApi.ApiErrorMapper
import com.desuzed.everyweather.data.network.dto.weatherApi.ErrorDtoWeatherApi
import com.desuzed.everyweather.data.network.dto.weatherApi.WeatherResponseDto
import com.desuzed.everyweather.data.network.dto.weatherApi.WeatherResponseMapper
import com.desuzed.everyweather.data.network.retrofit.NetworkResponse
import com.desuzed.everyweather.data.repository.local.ContextProvider
import com.desuzed.everyweather.data.repository.local.LocalDataSource
import com.desuzed.everyweather.data.repository.local.RoomProvider
import com.desuzed.everyweather.data.repository.remote.RemoteDataSource
import com.desuzed.everyweather.data.room.FavoriteLocationDto
import com.desuzed.everyweather.domain.model.WeatherResponse
import com.desuzed.everyweather.presentation.ui.next_days.NextDaysUi
import com.desuzed.everyweather.presentation.ui.main.WeatherMainUi
import kotlinx.coroutines.flow.Flow

class RepositoryAppImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : RepositoryApp {
    //RoomProvider
    override suspend fun insert(favoriteLocationDto: FavoriteLocationDto): Boolean =
        localDataSource.provideRoom().insert(favoriteLocationDto)

    override suspend fun deleteItem(favoriteLocationDto: FavoriteLocationDto): Boolean =
        localDataSource.provideRoom().deleteItem(favoriteLocationDto)


    override suspend fun containsPrimaryKey(latLon: String): Boolean =
        localDataSource.provideRoom().containsPrimaryKey(latLon)

    override fun getAllLocations(): Flow<List<FavoriteLocationDto>> =
        localDataSource.provideRoom().getAllLocations()

    //ContextProvider
    override fun saveForecastToCache(weatherResponse: WeatherResponse) =
        localDataSource.getContextProvider().saveForecastToCache(weatherResponse)

    override fun loadForecastFromCache(): WeatherResponse? =
        localDataSource.getContextProvider().loadForecastFromCache()

    override fun saveQuery(query: String) = localDataSource.getContextProvider().saveQuery(query)

    override fun loadQuery(): String = localDataSource.getContextProvider().loadQuery()

    override fun parseCode(errorCode: Int): String =
        localDataSource.getContextProvider().parseCode(errorCode)

    override suspend fun mapToNextDaysUi(response: WeatherResponse): List<NextDaysUi> {
        return localDataSource.getContextProvider().mapToNextDaysUi(response)
    }

    override suspend fun mapToMainWeatherUi(response: WeatherResponse): WeatherMainUi =
        localDataSource.getContextProvider().mapToMainWeatherUi(response)

    override suspend fun getForecast(query: String): NetworkResponse<WeatherResponseDto, ErrorDtoWeatherApi> {
        saveQuery(query)
        return remoteDataSource.getForecast(query)
    }

    override fun getNetworkConnection(): Flow<Boolean> =
        localDataSource.getContextProvider().getNetworkConnection()

    //todo вынести в какой нибудь юзкейс и избавиться от репозиторий слоя
    override suspend fun fetchForecastOrErrorMessage(query: String): ResultForecast {
        if (query.isEmpty()) {
            return ResultForecast(null, parseCode(ActionResultProvider.NO_DATA))
        }
        return when (val response = getForecast(query)) {
            is NetworkResponse.Success -> {
                val weatherResponse = WeatherResponseMapper.mapFromEntity(response.body)
                saveForecastToCache(weatherResponse)
                ResultForecast(weatherResponse, null)
            }
            is NetworkResponse.ApiError -> {
                val apiError = ApiErrorMapper().mapFromEntity(response.body)
                ResultForecast(loadForecastFromCache(), parseCode(apiError.error.code))
            }
            is NetworkResponse.NetworkError -> ResultForecast(
                loadForecastFromCache(),
                parseCode(ActionResultProvider.NO_INTERNET)
            )
            is NetworkResponse.UnknownError -> ResultForecast(
                loadForecastFromCache(),
                parseCode(ActionResultProvider.UNKNOWN)
            )
        }
    }

}

interface RepositoryApp : RoomProvider, ContextProvider, RemoteDataSource {
    suspend fun fetchForecastOrErrorMessage(query: String): ResultForecast
}