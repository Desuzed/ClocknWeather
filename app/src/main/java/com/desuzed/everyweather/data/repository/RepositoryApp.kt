package com.desuzed.everyweather.data.repository

import androidx.lifecycle.LiveData
import com.desuzed.everyweather.data.network.dto.weatherApi.ErrorDtoWeatherApi
import com.desuzed.everyweather.data.network.dto.weatherApi.WeatherResponseDto
import com.desuzed.everyweather.data.network.retrofit.NetworkResponse
import com.desuzed.everyweather.data.room.FavoriteLocationDto
import com.desuzed.everyweather.model.NetworkLiveData
import com.desuzed.everyweather.model.model.WeatherResponse

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

    override fun getAllLocations(): LiveData<List<FavoriteLocationDto>> =
        localDataSource.provideRoom().getAllLocations()

    //SPrefProvider
    override fun saveForecast(weatherResponse: WeatherResponse) =
        localDataSource.provideSPref().saveForecast(weatherResponse)

    override fun loadForecast(): WeatherResponse? = localDataSource.provideSPref().loadForecast()

    override fun saveQuery(query: String) = localDataSource.provideSPref().saveQuery(query)

    override fun loadQuery(): String = localDataSource.provideSPref().loadQuery()

    override fun parseCode(errorCode: Int): String =
        localDataSource.provideSPref().parseCode(errorCode)

    override suspend fun getForecast(query: String): NetworkResponse<WeatherResponseDto, ErrorDtoWeatherApi> {
        saveQuery(query)
        return remoteDataSource.getForecast(query)
    }

    //TODO refactor networkLiveData
    override fun getNetworkLiveData(): NetworkLiveData = localDataSource.getNetworkLiveData()

    //TODO Имплементировать когда появится второй апи
//    suspend fun getForecast1(query: String): Pair <WeatherResponse? , Int>{
//        saveQuery(query)
//        return when (val response = getForecastMock(query)) {
//            is NetworkResponse.Success -> {
//                val weatherResponse1 = if (response.body is WeatherResponseDto){
//                    WeatherResponseMapper().mapFromEntity(response.body)
//                }else {
//                    WeatherResponseMapper().mapFromEntity(response.body as WeatherResponseDto)
//                    //TODO not implemented
//                }
//                val weatherResponse = WeatherResponseMapper().mapFromEntity(response.body as WeatherResponseDto)
//                saveForecast(weatherResponse)
//                Pair(weatherResponse, -10) //TODO success
//            }
//            is NetworkResponse.ApiError -> {
//                //TODO По аналогии как вверху
//                val apiError = ApiErrorMapper().mapFromEntity(response.body as ErrorDtoWeatherApi)
//                Pair(null, apiError.error.code)
//            }
//            is NetworkResponse.NetworkError ->Pair(null,  ActionResultProvider.NO_INTERNET)
//            is NetworkResponse.UnknownError -> Pair(null,  ActionResultProvider.UNKNOWN)
//
//        }
//    }
//
    //    suspend fun getForecastMock(query: String): NetworkResponse<ApiTypeWeather, ApiTypeError> {
//        //        if (mApiType is ApiType.WeatherApi) {
//            return WeatherApiService
//                .getInstance()
//               .getForecast(query, lang)
//        } else if (mApiType is ApiType.OpenWeatherApi) ..... запускаем другой сервис
//        return WeatherApiService
//            .getInstance()
//            .getForecast(query, "en")
//    }
}

interface RepositoryApp : RoomProvider, SPrefProvider, RemoteDataSource {
    fun getNetworkLiveData(): NetworkLiveData
}