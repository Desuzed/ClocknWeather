package com.desuzed.everyweather.domain.model.settings

enum class SettingsType {
    LANG,
    DARK_MODE,
    DISTANCE,
    TEMP,
}

enum class Lang(val lang: String) {
    RU("RU"), EN("EN")
}

enum class TempDimen(val dimensionName: String) {
    FAHRENHEIT("FAHRENHEIT"), CELCIUS("CELCIUS")
}

//todo метры в секунду переводить в километры в секунду на стороне мобилы, также с милями
enum class DistanceDimen(val dimensionName: String) {
    METRIC("METRIC"), IMPERIAL("IMPERIAL")
}

enum class DarkMode(val mode: String) {
    ON("ON"), OFF("OFF"), SYSTEM("SYSTEM")
}