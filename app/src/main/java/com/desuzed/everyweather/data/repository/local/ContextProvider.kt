package com.desuzed.everyweather.data.repository.local

import android.content.Context
import com.desuzed.everyweather.util.ActionResultProviderImpl
import com.desuzed.everyweather.domain.model.WeatherResponse
import com.desuzed.everyweather.presentation.ui.main.MainWeatherMapper
import com.desuzed.everyweather.presentation.ui.main.WeatherMainUi
import com.desuzed.everyweather.presentation.ui.next_days.NextDaysMapper
import com.desuzed.everyweather.presentation.ui.next_days.NextDaysUi
import com.desuzed.everyweather.util.NetworkConnection
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ContextProviderImpl(
    private val context: Context,
    private val networkConnection: NetworkConnection
) : ContextProvider {
    private val sp =
        context.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE)

    override fun saveForecastToCache(weatherResponse: WeatherResponse) {
        val gson = Gson().toJson(weatherResponse)
        sp
            .edit()
            .putString(WEATHER_API, gson)
            .apply()
    }

    override fun loadForecastFromCache(): WeatherResponse? {
        val savedForecast = sp.getString(WEATHER_API, null)
        return Gson().fromJson(savedForecast, WeatherResponse::class.java)
    }

    override fun saveQuery(query: String) {
        sp
            .edit()
            .putString(QUERY, query)
            .apply()
    }

    override fun loadQuery(): String = sp.getString(QUERY, "").toString()

    override fun parseCode(errorCode: Int): String =
        ActionResultProviderImpl(context.resources).parseCode(errorCode)

    override fun getNetworkConnection(): Flow<Boolean> = networkConnection.hasInternetFlow()

    override suspend fun mapToNextDaysUi(response: WeatherResponse): List<NextDaysUi> =
        withContext(Dispatchers.IO) {
            NextDaysMapper(context.resources).mapToNextDaysList(response)
        }

    override suspend fun mapToMainWeatherUi(response: WeatherResponse): WeatherMainUi =
        withContext(Dispatchers.IO) {
            MainWeatherMapper(context.resources).mapToMainWeatherUi(response)
        }

    companion object {
        const val S_PREF_NAME = "SP"
        const val WEATHER_API = "WEATHER_API"
        const val QUERY = "QUERY"
    }

}

//todo вынести в domain слой
interface ContextProvider {
    fun saveForecastToCache(weatherResponse: WeatherResponse)
    fun loadForecastFromCache(): WeatherResponse?
    fun saveQuery(query: String)
    fun loadQuery(): String
    fun parseCode(errorCode: Int): String
    fun getNetworkConnection(): Flow<Boolean>
    suspend fun mapToNextDaysUi(response: WeatherResponse): List<NextDaysUi>
    suspend fun mapToMainWeatherUi(response: WeatherResponse): WeatherMainUi
}