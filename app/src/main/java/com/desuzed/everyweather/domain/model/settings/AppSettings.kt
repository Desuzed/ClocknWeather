package com.desuzed.everyweather.domain.model.settings

data class AppSettings(
    val lang: Language = Language.RU,
    val darkMode: DarkMode = DarkMode.SYSTEM,
    val distanceDimension: DistanceDimension = DistanceDimension.METRIC,
    val temperatureDimension: TemperatureDimension = TemperatureDimension.CELCIUS,
    //todo pressureDimension: mb, inches, millimeters
)