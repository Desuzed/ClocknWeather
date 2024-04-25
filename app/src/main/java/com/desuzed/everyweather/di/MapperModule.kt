package com.desuzed.everyweather.di

import com.desuzed.everyweather.data.mapper.location.FavoriteLocationMapper
import com.desuzed.everyweather.data.mapper.location.LocationResponseMapper
import com.desuzed.everyweather.data.mapper.location.UserLatLngMapper
import com.desuzed.everyweather.data.mapper.weather_api.ApiErrorMapper
import com.desuzed.everyweather.data.mapper.weather_api.AstroMapper
import com.desuzed.everyweather.data.mapper.weather_api.CurrentMapper
import com.desuzed.everyweather.data.mapper.weather_api.DayMapper
import com.desuzed.everyweather.data.mapper.weather_api.ForecastDayMapper
import com.desuzed.everyweather.data.mapper.weather_api.HourMapper
import com.desuzed.everyweather.data.mapper.weather_api.LocationMapper
import com.desuzed.everyweather.data.mapper.weather_api.WeatherResponseMapper
import org.koin.dsl.module

val mapperModule = module {
    factory { ApiErrorMapper() }
    factory { AstroMapper() }
    factory { CurrentMapper() }
    factory { DayMapper() }
    factory { HourMapper() }
    factory { LocationMapper() }
    factory {
        ForecastDayMapper(
            dayMapper = get(),
            hourMapper = get(),
            astroMapper = get()
        )
    }
    factory {
        WeatherResponseMapper(
            locationMapper = get(),
            currentMapper = get(),
            forecastDayMapper = get()
        )
    }

    factory { LocationResponseMapper() }
    factory { FavoriteLocationMapper() }
    factory { UserLatLngMapper() }

}