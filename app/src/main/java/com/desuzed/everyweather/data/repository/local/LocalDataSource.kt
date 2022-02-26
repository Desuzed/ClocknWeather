package com.desuzed.everyweather.data.repository.local

import android.content.Context
import com.desuzed.everyweather.model.NetworkLiveData

class LocalDataSourceImpl(
    private val roomProvider: RoomProvider,
    private val context: Context,
    private val sPrefProvider: SPrefProvider
) :
    LocalDataSource {
    override fun provideRoom(): RoomProvider = roomProvider
    override fun provideSPref(): SPrefProvider = sPrefProvider

    private val networkLiveData = NetworkLiveData(context)

    override fun getNetworkLiveData(): NetworkLiveData {
        return networkLiveData
    }

    companion object {
        const val S_PREF_NAME = "SP"
        const val WEATHER_API = "WEATHER_API"
        const val QUERY = "QUERY"
    }

}

interface LocalDataSource {
    fun provideRoom(): RoomProvider
    fun provideSPref(): SPrefProvider
    fun getNetworkLiveData(): NetworkLiveData
}
