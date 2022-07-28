package com.desuzed.everyweather.data.network.dto.weatherApi

import com.desuzed.everyweather.domain.model.Current
import com.desuzed.everyweather.util.EntityMapper
import com.google.gson.annotations.SerializedName

class CurrentDto {
    @SerializedName("last_updated_epoch")
    var lastUpdatedEpoch: Long = 0

    @SerializedName("last_updated")
    var lastUpdated: String = ""

    @SerializedName("temp_c")
    var temp = 0f

    @SerializedName("condition")
    var conditionDto: ConditionDto? = null

    @SerializedName("wind_kph")
    var windSpeed = 0f

    @SerializedName("wind_degree")
    var windDegree: Int = 0

    @SerializedName("wind_dir")
    var windDir: String = ""

    @SerializedName("pressure_mb")
    var pressureMb = 0f

    @SerializedName("precip_mm")
    var precipMm = 0f

    @SerializedName("humidity")
    var humidity: Int = 0

    @SerializedName("cloud")
    var cloud: Int = 0

    @SerializedName("feelslike_c")
    var feelsLike = 0f

    @SerializedName("vis_km")
    var vis = 0f

    @SerializedName("uv")
    var uv = 0f

    @SerializedName("gust_kph")
    var gust = 0f

}

class CurrentMapper : EntityMapper<CurrentDto, Current> {
    override fun mapFromEntity(entity: CurrentDto): Current {
        return Current(
            entity.temp,
            entity.conditionDto?.text.toString(),
            entity.conditionDto?.icon.toString(),
            entity.windSpeed,
            entity.windDegree,
            entity.windDir,
            entity.pressureMb,
            entity.precipMm,
            entity.humidity,
            entity.feelsLike
        )
    }
}