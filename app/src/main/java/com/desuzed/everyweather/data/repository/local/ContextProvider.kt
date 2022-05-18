package com.desuzed.everyweather.data.repository.local

import android.content.Context
import com.desuzed.everyweather.data.network.ActionResultProviderImpl
import com.desuzed.everyweather.model.entity.WeatherResponse
import com.desuzed.everyweather.view.entity.HourEntityView
import com.desuzed.everyweather.view.entity.HourUi
import com.desuzed.everyweather.view.entity.NextDaysEntityView
import com.desuzed.everyweather.view.ui.DetailCard
import com.desuzed.everyweather.view.ui.NextDaysUi
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContextProviderImpl(private val context: Context) : ContextProvider {
    private val sp =
        context.getSharedPreferences(LocalDataSourceImpl.S_PREF_NAME, Context.MODE_PRIVATE)

    override fun saveForecastToCache(weatherResponse: WeatherResponse) {
        val gson = Gson().toJson(weatherResponse)
        sp
            .edit()
            .putString(LocalDataSourceImpl.WEATHER_API, gson)
            .apply()
    }

    override fun loadForecastFromCache(): WeatherResponse? {
        val savedForecast = sp.getString(LocalDataSourceImpl.WEATHER_API, null)
        return Gson().fromJson(savedForecast, WeatherResponse::class.java)
    }

    override fun saveQuery(query: String) {
        sp
            .edit()
            .putString(LocalDataSourceImpl.QUERY, query)
            .apply()
    }

    override fun loadQuery(): String = sp.getString(LocalDataSourceImpl.QUERY, "").toString()

    override fun parseCode(errorCode: Int): String =
        ActionResultProviderImpl(context.resources).parseCode(errorCode)

    override suspend fun mapToNextDaysUi(response: WeatherResponse): List<NextDaysUi> =
        withContext(Dispatchers.IO) {
            //TODO REFACTORING to mapper
            val resultList = ArrayList<NextDaysUi>()
            response.forecastDay.forEach {
                val entityView =
                    NextDaysEntityView(it, response.location.timezone, context.resources)
                val hourList = ArrayList<HourUi>()
                it.hourForecast.forEach {
                    val hourEntity =
                        HourEntityView(it, response.location.timezone, context.resources)
                    hourList.add(
                        HourUi(
                            time = hourEntity.time,
                            temp = hourEntity.temp,
                            wind = hourEntity.wind,
                            iconUrl = hourEntity.iconUrl,
                            rotation = hourEntity.rotation
                        )
                    )
                }
                resultList.add(
                    NextDaysUi(
                        iconUrl = entityView.iconUrl,
                        date = entityView.date,
                        description = entityView.description,
                        maxTemp = entityView.maxTemp,
                        minTemp = entityView.minTemp,
                        detailCard = DetailCard(
                            wind = entityView.wind,
                            pressure = entityView.pressure,
                            humidity = entityView.humidity,
                            pop = entityView.pop,
                            sun = entityView.sun,
                            moon = entityView.moon
                        ),
                        hourList = hourList,
                    )
                )
            }
            resultList
        }
}

interface ContextProvider {
    fun saveForecastToCache(weatherResponse: WeatherResponse)
    fun loadForecastFromCache(): WeatherResponse?
    fun saveQuery(query: String)
    fun loadQuery(): String
    fun parseCode(errorCode: Int): String
    suspend fun mapToNextDaysUi(response: WeatherResponse): List<NextDaysUi>
}