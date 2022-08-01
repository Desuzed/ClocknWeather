package com.desuzed.everyweather.di

import com.desuzed.everyweather.data.mapper.*
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

}