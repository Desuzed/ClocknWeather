package com.desuzed.everyweather.domain.model.settings

/**
 * @property id - айди настройки из Enum'a. Используется для сохранения в Shared prefs  в виде строки
 * По этой строке инициализируется Enum настройки
 * @property categoryStringId - айди ресурса категории настройки (например "Язык")
 * @property valueStringId - айди значения настройки (например "Русский")
 * @property type - тип настройки (например язык или температура)
 * */
abstract class BaseSettingItem {
    abstract val id: String
    abstract val categoryStringId: Int
    abstract val valueStringId: Int
    abstract val type: SettingsType
}