package com.desuzed.everyweather.domain.model.settings

const val APP_SETTING = 1
const val DIMENSION_SETTING = 2

enum class SettingsType(category: Int) {
    LANG(category = APP_SETTING),
    DARK_MODE (category = APP_SETTING),
    DISTANCE(category = DIMENSION_SETTING),
    TEMP(category = DIMENSION_SETTING),
}

enum class Language(val lang: String) {
    RU("ru"), EN("en")
}

enum class TemperatureDimension(val dimensionName: String) {
    FAHRENHEIT("fahrenheit"), CELCIUS("celcius")
}

//todo метры в секунду переводить в километры в секунду на стороне мобилы, также с милями
enum class DistanceDimension(val dimensionName: String) {
    METRIC("METRIC"), IMPERIAL("IMPERIAL")
}

enum class DarkMode(val mode: String) {
    ON("ON"), OFF("OFF"), SYSTEM("SYSTEM")
}