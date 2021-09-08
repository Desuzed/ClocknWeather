package com.desuzed.clocknweather.retrofit

import com.google.gson.annotations.SerializedName

class FiveDayForecast {
    @SerializedName("cod")
    var cod: String? = null

    @SerializedName("message")
    var message = 0

    @SerializedName("cnt")
    var cnt = 0

    @SerializedName("list")
    var list: List? = null

    class List {
        @SerializedName("dt")
        var dt: Long = 0

        @SerializedName("dt_txt")
        var dt_txt: String? = null

        @SerializedName("main")
        var main: Main? = null

        @SerializedName("weather")
        var weather: Weather? = null

        class Main {
            @SerializedName("temp")
            var temp = 0f

            @SerializedName("feels_like")
            var feels_like = 0f

            @SerializedName("temp_min")
            var temp_min = 0f

            @SerializedName("temp_max")
            var temp_max = 0f

            @SerializedName("pressure")
            var pressure: Int = 0

            @SerializedName("sea_level")
            var sea_level: Int = 0

            @SerializedName("grnd_level")
            var grnd_level: Int = 0

            @SerializedName("humidity")
            var humidity: Int = 0

        }

        class Wind {
            @SerializedName("speed")
            var speed = 0f

            @SerializedName("deg")
            var deg: Int = 0

            @SerializedName("gust")
            var gust = 0f
        }

        class Weather {
            @SerializedName("id")
            var id = 0

            @SerializedName("main")
            var main: String? = null

            @SerializedName("description")
            var description: String? = null

            @SerializedName("icon")
            var icon: String? = null
            override fun toString(): String {
                return "main=$main\ndescription=$description"
            }
        }
    }

    class City {
        @SerializedName("id")
        var id = 0

        @SerializedName("name")
        var name: String? = null

        @SerializedName("sunrise")
        var sunrise: Long = 0

        @SerializedName("sunset")
        var sunset: Long = 0

        @SerializedName("country")
        var country: String? = null


        class Coord {
            @SerializedName("lat")
            var lat = 0f

            @SerializedName("lon")
            var lon = 0f
        }
    }


}

