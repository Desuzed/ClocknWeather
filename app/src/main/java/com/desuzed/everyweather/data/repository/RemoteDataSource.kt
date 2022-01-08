package com.desuzed.everyweather.data.repository

import com.desuzed.everyweather.data.network.dto.ApiTypeWeather
import com.desuzed.everyweather.data.network.dto.weatherApi.ErrorDtoWeatherApi
import com.desuzed.everyweather.data.network.dto.weatherApi.WeatherResponseDto
import com.desuzed.everyweather.data.network.retrofit.NetworkResponse
import com.desuzed.everyweather.data.network.retrofit.WeatherApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteDataSourceImpl : RemoteDataSource {
    /**
     * Default language: English
     */
    var lang: String = "en"
  //  var mApiType: ApiTypeWeather = ApiTypeWeather.WeatherApi()

    override suspend fun getForecast(query: String): NetworkResponse<WeatherResponseDto, ErrorDtoWeatherApi> =
        withContext(Dispatchers.IO) {
            WeatherApiService
                .getInstance()
                .getForecast(query, lang)
        }


//    suspend fun getForecast1(query: String): NetworkResponse<ApiTypeWeather, ApiTypeError> {
//        //        if (mApiType is ApiType.WeatherApi) {
////            return WeatherApiService
////                .getInstance()
////                .getForecast(query, lang)
////        } else if (mApiType is ApiType.OpenWeatherApi) ..... запускаем другой сервис
//        return WeatherApiService
//            .getInstance()
//            .getForecast(query, lang)
//    }

    /**
     * Here will be calls from other weather APIs
     */
}


interface RemoteDataSource {
    suspend fun getForecast(query: String): NetworkResponse<WeatherResponseDto, ErrorDtoWeatherApi>
}