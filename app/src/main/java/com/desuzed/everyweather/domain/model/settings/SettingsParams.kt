package com.desuzed.everyweather.domain.model.settings

enum class SettingsType {
    LANG,
    DARK_MODE,
    DISTANCE,
    TEMP,
    PRESSURE,
}

enum class Lang(val lang: String) {
    RU("RU"), EN("EN")
}

enum class DarkMode(val mode: String) {
    ON("ON"), OFF("OFF"), SYSTEM("SYSTEM")
}

enum class TempDimen(val dimensionName: String) {
    FAHRENHEIT("FAHRENHEIT"), CELCIUS("CELCIUS")
}

enum class DistanceDimen(val dimensionName: String) {
    METRIC_KMH("METRIC_KMH"),
    METRIC_MS("METRIC_MS"),
    IMPERIAL("IMPERIAL"),
}

enum class PressureDimen(val dimensionName: String) {
    MILLIBAR("MILLIBAR"),
    MILLIMETERS("MILLIMETERS"),
    INCHES("INCHES"),
}