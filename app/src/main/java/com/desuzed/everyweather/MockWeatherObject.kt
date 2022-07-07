package com.desuzed.everyweather

import com.desuzed.everyweather.data.room.FavoriteLocationDto
import com.desuzed.everyweather.model.entity.*

object MockWeatherObject {
    val weather = WeatherResponse(
        Location(
            "Location name",
            "region location",
            "country",
            100.5f,
            200.1f,
            "Moscow/russia",
            10000000,
            "10:50"
        ),
        Current(25.1f, "text", "icon", 100.1f, 60, "West", 273f, 100f, 200, 28.5f),
        listOf(
            ForecastDay(
                "11.11.2021",
                13231312,
                Day(27.3f, 21.3f, 100f, 200f, 125f, 50, 40, "text", "icon"),
                Astro("11:22", "19:05", "10:00", "11:22"),
                listOf(Hour(123122545, 20.1f, "text", "ic", 120f, 22, 543f)),
                false
            ),
            ForecastDay(
                "11.11.2021",
                13231312,
                Day(27.3f, 21.3f, 100f, 200f, 125f, 50, 40, "text", "icon"),
                Astro("11:22", "19:05", "10:00", "11:22"),
                listOf(Hour(123122545, 20.1f, "text", "ic", 120f, 22, 543f)),
                false
            )
        )
    )
    val location = FavoriteLocationDto("", "London", "London region", "United Kingdoms", "", "")
    val locations = listOf(location, location, location, location, location, location, location)
}