package com.desuzed.everyweather.data.room

import com.desuzed.everyweather.domain.model.weather.Location
import junit.framework.TestCase

class FavoriteLocationDtoTest : TestCase() {

    fun `test toString returns correct value with all country fields being filled`() {
        val favoriteLocationDto =
            FavoriteLocationDto(
                "43.2,132.1",
                "cityName",
                "region",
                "country",
                "43.2",
                "132.1"
            )
        val expectedStr = "region, country"

        val actualStr = favoriteLocationDto.toString()

        assertEquals(expectedStr, actualStr)
    }

    fun `test toString returns correct value when region is empty`() {
        val favoriteLocationDto =
            FavoriteLocationDto(
                "43.2,132.1",
                "cityName",
                "",
                "country",
                "43.2",
                "132.1"
            )
        val expectedStr = "country"

        val actualStr = favoriteLocationDto.toString()

        assertEquals(expectedStr, actualStr)
    }

    fun `test generated key returns correct value`() {
        val location =
            Location(
                "name",
                "region",
                "country",
                43.19f, 132.02f,
                "timezone",
                10,
            )
        val expected = "43.2,132.1"

        val actual = FavoriteLocationDto.generateKey(location)

        assertEquals(expected, actual)

    }

}