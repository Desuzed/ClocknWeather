package com.desuzed.everyweather.data.repository.local

class LocalDataSourceImpl(
    private val roomProvider: RoomProvider,
    private val contextProvider: ContextProvider
) :
    LocalDataSource {
    override fun provideRoom(): RoomProvider = roomProvider
    override fun getContextProvider(): ContextProvider = contextProvider

}

interface LocalDataSource {
    fun provideRoom(): RoomProvider
    fun getContextProvider(): ContextProvider
}
