package com.desuzed.everyweather.data.repository.providers

import android.content.res.Resources
import com.desuzed.everyweather.data.repository.providers.action_result.GeoActionResultProvider
import com.desuzed.everyweather.data.repository.providers.action_result.WeatherActionResultProvider
import com.desuzed.everyweather.domain.repository.provider.ActionResultProvider
import kotlin.reflect.KClass

object ActionResultProviderFactory {

    fun <P : ActionResultProvider> provide(
        providerClass: KClass<P>,
        resources: Resources,
    ): ActionResultProvider {
        return when (providerClass) {
            WeatherActionResultProvider::class -> WeatherActionResultProvider(resources)
            GeoActionResultProvider::class -> GeoActionResultProvider(resources)
            else -> throw IllegalStateException("ActionResultProvider not provided")
        }
    }
}